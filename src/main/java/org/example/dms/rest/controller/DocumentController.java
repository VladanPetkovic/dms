package org.example.dms.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.dms.rest.model.Document;
import org.example.dms.rest.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Operation(summary = "Upload a new document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Document> uploadDocument(
            @Parameter(description = "Document metadata") @ModelAttribute Document document,
            @Parameter(description = "File to upload") @RequestParam MultipartFile file) {
        Document savedDocument = documentService.saveDocumentFile(document, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDocument);
    }

    @Operation(summary = "Get all documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved all documents")
    })
    @GetMapping("/documents")
    public ResponseEntity<Page<Document>> getAllDocuments(
            @RequestParam(required = false) String name,            // Filter by name (optional)
            @RequestParam(defaultValue = "0") int page,             // Page number (defaults to 0)
            @RequestParam(defaultValue = "10") int maxCountDocuments // Max documents per page (defaults to 10)
    ) {
        Pageable pageable = PageRequest.of(page, maxCountDocuments);
        Page<Document> documentPage = documentService.getDocumentsByName(name, pageable);
        return ResponseEntity.ok(documentPage);
    }

    @Operation(summary = "Get document by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document found"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@Parameter(description = "ID of the document") @PathVariable Long id) {
        Optional<Document> document = documentService.getDocumentById(id);
        return document.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Update document by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document updated successfully"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(
            @Parameter(description = "ID of the document to update") @PathVariable Long id,
            @Parameter(description = "Updated document data") @RequestBody Document updatedDocument) {
        Document document = documentService.updateDocument(id, updatedDocument);
        return ResponseEntity.ok(document);
    }

    @Operation(summary = "Delete document by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@Parameter(description = "ID of the document to delete") @PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
