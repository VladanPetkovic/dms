package org.example.dms.rest.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.example.dms.rest.dto.DocumentDTO;
import org.example.dms.rest.mapper.DocumentMapper;
import org.example.dms.rest.model.Document;
import org.example.dms.rest.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DocumentService {
    private static final Logger logger = LogManager.getLogger(DocumentService.class);
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    private final DocumentMapper mapper = DocumentMapper.INSTANCE;
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public DocumentDTO saveDocument(DocumentDTO documentDTO, MultipartFile file) {
        logger.info("Saving document file: {}", file.getOriginalFilename());
        try {
            Document document = mapper.toDocument(documentDTO);
            document.setPath("TODO"); // TODO: set the actual path
            document.setCreated_at(LocalDateTime.now());
            document.setType(file.getContentType());
            validateDocument(document);
            Document savedDocument = documentRepository.save(document);
            return mapper.toDocumentDTO(savedDocument);
        } catch (Exception e) {
            logger.error("Error while saving document file", e);
            throw new RuntimeException("Failed to save document file", e);
        }
    }

    public List<DocumentDTO> saveDocuments(List<DocumentDTO> documentDTOs) {
        logger.info("Attempting to save a batch of documents, count: {}", documentDTOs.size());
        List<Document> documents = documentDTOs.stream()
                .map(mapper::toDocument)
                .collect(Collectors.toList());
        List<Document> savedDocuments = documentRepository.saveAll(documents);
        logger.info("Batch of documents saved successfully, total: {}", savedDocuments.size());
        return savedDocuments.stream()
                .map(mapper::toDocumentDTO)
                .collect(Collectors.toList());
    }

    public List<DocumentDTO> getAllDocuments() {
        logger.info("Fetching all documents");
        List<Document> documents = documentRepository.findAll();
        logger.info("Retrieved all documents, count: {}", documents.size());
        return documents.stream()
                .map(mapper::toDocumentDTO)
                .collect(Collectors.toList());
    }

    public Optional<DocumentDTO> getDocumentById(Long id) {
        logger.info("Fetching document by ID: {}", id);
        Optional<Document> document = documentRepository.findById(id);
        if (document.isPresent()) {
            logger.info("Document found with ID: {}", id);
            return document.map(mapper::toDocumentDTO);
        } else {
            logger.warn("Document not found with ID: {}", id);
            return Optional.empty();
        }
    }

    public Page<DocumentDTO> getDocumentsByName(String name, Pageable pageable) {
        logger.info("Fetching documents by name: {}", name);
        Page<Document> documentPage;
        if (name == null || name.isEmpty()) {
            documentPage = documentRepository.findAll(pageable);
        } else {
            documentPage = documentRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        return documentPage.map(mapper::toDocumentDTO);
    }

    public DocumentDTO updateDocument(Long id, DocumentDTO updatedDocumentDTO) {
        logger.info("Attempting to update document with ID: {}", id);
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
            logger.info("Document updated successfully for ID: {}", id);
            return mapper.toDocumentDTO(savedDocument);
        } else {
            logger.error("Failed to find document with ID: {}", id);
            throw new RuntimeException("Document not found");
        }
    }

    public void deleteDocument(Long id) {
        logger.info("Attempting to delete document with ID: {}", id);
        try {
            documentRepository.deleteById(id);
            logger.info("Document deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("Failed to delete document with ID: {}", id, e);
            throw e;
        }
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
            logger.error(violation.getMessage());
        }
        throw new IllegalArgumentException(errorMessage.toString());
    }
}
