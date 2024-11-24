package org.example.paperless_rest.service;

import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.example.paperless_rest.app.FileUpload;
import org.example.paperless_rest.dto.DocumentDTO;
import org.example.paperless_rest.exception.DocumentValidationException;
import org.example.paperless_rest.exception.StorageException;
import org.example.paperless_rest.mapper.DocumentMapper;
import org.example.paperless_rest.model.Document;
import org.example.paperless_rest.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final StorageService storageService;
    private final QueueProducerService queueProducerService;

    @Autowired
    public DocumentService(DocumentRepository documentRepository, StorageService storageService, QueueProducerService qp) {
        this.documentRepository = documentRepository;
        this.storageService = storageService;
        this.queueProducerService = qp;
    }

    private final DocumentMapper mapper = DocumentMapper.INSTANCE;
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public DocumentDTO saveDocument(DocumentDTO documentDTO, MultipartFile file) {
        log.info("Saving document file: {}", file.getOriginalFilename());
        try {
            Document document = mapper.toDocument(documentDTO);
            String uniqueFileName = FileUpload.getUniqueFileName(file);
            document.setPath(uniqueFileName);
            document.setCreated_at(LocalDateTime.now());
            document.setType(file.getContentType());
            validateDocument(document);
            storageService.store(file, uniqueFileName);
            Document savedDocument = documentRepository.save(document);
            queueProducerService.sendMessage(uniqueFileName);
            return mapper.toDocumentDTO(savedDocument);
        } catch (DocumentValidationException e) {
            log.error("Validation error while saving document ", e);
            throw new DocumentValidationException(e.getMessage());
        } catch (StorageException e) {
            log.error("Storage error while saving document ", e);
            throw new StorageException(e.getMessage());
        } catch (Exception e) {
            log.error("Error while saving document ", e);
            throw new RuntimeException("Failed to save document ", e);
        }
    }

    public List<DocumentDTO> getAllDocuments() {
        log.info("Fetching all documents");
        List<Document> documents = documentRepository.findAll();
        log.info("Retrieved all documents, count: {}", documents.size());
        return documents.stream()
                .map(mapper::toDocumentDTO)
                .collect(Collectors.toList());
    }

    public Optional<DocumentDTO> getDocumentById(Long id) {
        log.info("Fetching document by ID: {}", id);
        Optional<Document> document = documentRepository.findById(id);
        if (document.isPresent()) {
            log.info("Document found with ID: {}", id);
            return document.map(mapper::toDocumentDTO);
        } else {
            log.warn("Document not found with ID: {}", id);
            return Optional.empty();
        }
    }

    public Page<DocumentDTO> getDocumentsByName(String name, Pageable pageable) {
        log.info("Fetching documents by name: {}", name);
        Page<Document> documentPage;
        if (name == null || name.isEmpty()) {
            documentPage = documentRepository.findAll(pageable);
        } else {
            documentPage = documentRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        return documentPage.map(mapper::toDocumentDTO);
    }

    public DocumentDTO updateDocument(Long id, DocumentDTO updatedDocumentDTO) {
        log.info("Attempting to update document with ID: {}", id);
        Optional<Document> existingDocument = documentRepository.findById(id);
        if (existingDocument.isPresent()) {
            Document document = existingDocument.get();
            // Update document properties
            document.setName(updatedDocumentDTO.getName());
            document.setDescription(updatedDocumentDTO.getDescription());
            document.setUpdated_at(LocalDateTime.now());
            document.setPath(updatedDocumentDTO.getPath());
            document.setType(updatedDocumentDTO.getType()); // TODO: right now, it is not possible to update the file - therefor: change this

            validateDocument(document);
            Document savedDocument = documentRepository.save(document);
            log.info("Document updated successfully for ID: {}", id);
            return mapper.toDocumentDTO(savedDocument);
        } else {
            log.error("Failed to find document with ID: {}", id);
            throw new RuntimeException("Document not found");
        }
    }

    public void deleteDocument(Long id) {
        log.info("Attempting to delete document with ID: {}", id);
        Optional<Document> existingDocument = documentRepository.findById(id);
        try {
            if (existingDocument.isPresent()) {
                storageService.delete(existingDocument.get().getPath());
                documentRepository.deleteById(id);
                log.info("Document deleted successfully with ID: {}", id);
            }
        } catch (Exception e) {
            log.error("Failed to delete document with ID: {}", id, e);
            throw e;
        }
    }

    public Resource getDocumentFile(Long id) {
        String fileName = documentRepository.findByIdGetPath(id).orElse("");

        if (fileName.isEmpty()) {
            log.error("File path not found for document with ID: " + id);
            return null;
        }

        return storageService.load(fileName);
    }

    private void validateDocument(Document document) {
        Set<ConstraintViolation<Document>> violations = validator.validate(document);
        // return if no violations
        if (violations.isEmpty()) {
            return;
        }

        StringBuilder errorMessage = new StringBuilder("Validation failed for Document: ");
        for (ConstraintViolation<Document> violation : violations) {
            errorMessage.append(violation.getMessage()).append("; ");
            log.error(violation.getMessage());
        }
        throw new DocumentValidationException(errorMessage.toString());
    }
}
