package org.example.paperless_rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.example.paperless_rest.dto.DocumentDTO;
import org.example.paperless_rest.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/documents")
@Slf4j
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Operation(summary = "Upload a new document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PostMapping
    public ResponseEntity<DocumentDTO> uploadDocument(
            @Parameter(description = "Document metadata") @ModelAttribute DocumentDTO documentDTO,
            @Parameter(description = "File to upload (Only types: image/png, image/jpeg, image/jpg, application/pdf)") @RequestParam MultipartFile file) {
        log.info("Got document with name: " + documentDTO.getName());
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
        log.info("Filtering by: " + name);
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
        log.info("Given id: " + id);
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
        log.info("Given id: " + id);
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
        log.info("Given id: " + id);
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Download document by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(
            @Parameter(description = "ID of the document") @PathVariable Long id) {
        log.info("Requested download for document with id: " + id);
        Resource resource = documentService.getDocumentFile(id);
        Optional<DocumentDTO> documentDTO = documentService.getDocumentById(id);
        String contentType = null;
        String fileName = null;
        if (documentDTO.isPresent()) {
            contentType = documentDTO.get().getType();
            fileName = documentDTO.get().getName();
        }

        if (resource == null || contentType == null) {
            log.error("File not found: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
