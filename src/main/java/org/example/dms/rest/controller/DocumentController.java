package org.example.dms.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.dms.rest.dto.DocumentDTO;
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
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081", "http://localhost:80"}) // TODO: does not work
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
    public ResponseEntity<DocumentDTO> uploadDocument(
            @Parameter(description = "Document metadata") @ModelAttribute DocumentDTO documentDTO,
            @Parameter(description = "File to upload") @RequestParam MultipartFile file) {
        DocumentDTO savedDocumentDTO = documentService.saveDocument(documentDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDocumentDTO);
    }

    @Operation(summary = "Get all documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved all documents")
    })
    @GetMapping
    public ResponseEntity<Page<DocumentDTO>> getAllDocuments(
            @RequestParam(required = false) String name,            // Filter by name (optional)
            @RequestParam(defaultValue = "0") int page,             // Page number (defaults to 0)
            @RequestParam(defaultValue = "10") int maxCountDocuments // Max documents per page (defaults to 10)
    ) {
        Pageable pageable = PageRequest.of(page, maxCountDocuments);
        Page<DocumentDTO> documentPage = documentService.getDocumentsByName(name, pageable);
        return ResponseEntity.ok(documentPage);
    }

    @Operation(summary = "Get document by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document found"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@Parameter(description = "ID of the document") @PathVariable Long id) {
        Optional<DocumentDTO> documentDTO = documentService.getDocumentById(id);
        return documentDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Update document by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(
            @Parameter(description = "ID of the document to update") @PathVariable Long id,
            @Parameter(description = "Updated document data") @RequestBody DocumentDTO updatedDocumentDTO) {
        DocumentDTO documentDTO = documentService.updateDocument(id, updatedDocumentDTO);
        return ResponseEntity.ok(documentDTO);
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
