package org.example.dms.rest.service;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.example.dms.rest.model.Document;
import org.example.dms.rest.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {
    private static final Logger logger = LogManager.getLogger(DocumentService.class);
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * Save a single document.
     *
     * @param document the document to save.
     * @return the persisted document entity.
     */
    public Document saveDocument(Document document) {
        logger.info("Attempting to save document: {}", document.getName());
        Document savedDocument = documentRepository.save(document);
        logger.info("Document saved successfully with ID: {}", savedDocument.getId());
        return savedDocument;
    }

    /**
     * Save a batch of documents.
     *
     * @param documents the list of documents to save.
     * @return the list of persisted document entities.
     */
    public List<Document> saveDocuments(List<Document> documents) {
        logger.info("Attempting to save a batch of documents, count: {}", documents.size());
        documentRepository.saveAll(documents);
        logger.info("Batch of documents saved successfully, total: {}", documents.size());
        return documents;
    }

    /**
     * Get all documents.
     *
     * @return the list of documents.
     */
    public List<Document> getAllDocuments() {
        logger.info("Fetching all documents");
        List<Document> documents = documentRepository.findAll();
        logger.info("Retrieved all documents, count: {}", documents.size());
        return documents;
    }

    /**
     * Get one document by ID.
     *
     * @param id the ID of the document.
     * @return the document entity, if found.
     */
    public Optional<Document> getDocumentById(Long id) {
        logger.info("Fetching document by ID: {}", id);
        Optional<Document> document = documentRepository.findById(id);
        if (document.isPresent()) {
            logger.info("Document found with ID: {}", id);
        } else {
            logger.warn("Document not found with ID: {}", id);
        }
        return document;
    }

    /**
     * Update an existing document.
     *
     * @param id              the ID of the document to update.
     * @param updatedDocument the updated document data.
     * @return the updated document entity.
     */
    public Document updateDocument(Long id, Document updatedDocument) {
        logger.info("Attempting to update document with ID: {}", id);
        Optional<Document> existingDocument = documentRepository.findById(id);
        if (existingDocument.isPresent()) {
            Document document = existingDocument.get();
            // Update document properties
            document.setName(updatedDocument.getName());
            document.setDescription(updatedDocument.getDescription());
            document.setType(updatedDocument.getType());
            document.setData(updatedDocument.getData());
            document.setUpdated_at(updatedDocument.getUpdated_at());

            Document savedDocument = documentRepository.save(document);
            logger.info("Document updated successfully for ID: {}", id);
            return savedDocument;
        } else {
            logger.error("Failed to find document with ID: {}", id);
            throw new RuntimeException("Document not found");
        }
    }

    /**
     * Delete a document by ID.
     *
     * @param id the ID of the document.
     */
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

    /**
     * Save a document file, typically when uploading.
     *
     * @param document the document metadata.
     * @param file     the file to be saved.
     * @return the saved document entity.
     */
    public Document saveDocumentFile(Document document, MultipartFile file) {
        logger.info("Saving document file: {}", file.getOriginalFilename());
        try {
            document.setData(file.getBytes());
            document.setType(file.getContentType());
            return saveDocument(document);
        } catch (Exception e) {
            logger.error("Error while saving document file", e);
            throw new RuntimeException("Failed to save document file");
        }
    }
}
