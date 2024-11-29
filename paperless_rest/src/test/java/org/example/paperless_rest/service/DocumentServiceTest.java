package org.example.paperless_rest.service;

import org.example.paperless_rest.dto.DocumentDTO;
import org.example.paperless_rest.model.Document;
import org.example.paperless_rest.repository.DocumentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DocumentServiceTest {
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private StorageService storageService;

    @Mock
    private MultipartFile file; // Mocking MultipartFile for file upload tests

    @InjectMocks
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDocuments_ShouldReturnListOfDocumentDTOs() {
        // Arrange
        Document document = new Document();
        document.setId(1L);
        document.setName("Test Document");
        List<Document> documents = Collections.singletonList(document);

        when(documentRepository.findAll()).thenReturn(documents);

        // Act
        List<DocumentDTO> documentDTOs = documentService.getAllDocuments();

        // Assert
        assertEquals(1, documentDTOs.size());
        assertEquals("Test Document", documentDTOs.get(0).getName());
    }

    @Test
    void getDocumentById_ShouldReturnDocumentDTO_IfFound() {
        // Arrange
        Document document = new Document();
        document.setId(1L);
        document.setName("Test Document");

        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));

        // Act
        Optional<DocumentDTO> result = documentService.getDocumentById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Document", result.get().getName());
    }

    @Test
    void updateDocument_ShouldUpdateAndReturnDocumentDTO() {
        // Arrange
        Document existingDocument = new Document();
        existingDocument.setId(1L);
        existingDocument.setName("Old Name");
        existingDocument.setDescription("Old");

        DocumentDTO updatedDocumentDTO = new DocumentDTO();
        updatedDocumentDTO.setName("Updated");
        updatedDocumentDTO.setDescription("Updated");
        updatedDocumentDTO.setType("application/pdf");

        when(documentRepository.findById(1L)).thenReturn(Optional.of(existingDocument));
        when(documentRepository.save(any(Document.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        DocumentDTO result = documentService.updateDocument(1L, updatedDocumentDTO);

        // Assert
        assertEquals("Updated", result.getName());
        verify(documentRepository, times(1)).save(any(Document.class));
    }

    @Test
    void deleteDocument_Success() {
        // Arrange
        Long documentId = 1L;
        Document mockDocument = new Document();
        mockDocument.setId(documentId);
        mockDocument.setPath("test/path/to/document");

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(mockDocument));

        // Act
        documentService.deleteDocument(documentId);

        // Assert
        verify(storageService, times(1)).delete("test/path/to/document");
        verify(documentRepository, times(1)).deleteById(documentId);
    }

    @Test
    void testDeleteDocument_DocumentNotFound() {
        // Arrange
        Long documentId = 1L;

        when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

        // Act
        documentService.deleteDocument(documentId);

        // Assert
        verify(storageService, never()).delete(anyString());
        verify(documentRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteDocument_StorageServiceThrowsException() {
        // Arrange
        Long documentId = 1L;
        Document mockDocument = new Document();
        mockDocument.setId(documentId);
        mockDocument.setPath("test/path/to/document");

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(mockDocument));
        doThrow(new RuntimeException("Storage service failure")).when(storageService).delete(anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> documentService.deleteDocument(documentId));
        assertEquals("Storage service failure", exception.getMessage());

        verify(storageService, times(1)).delete("test/path/to/document");
        verify(documentRepository, never()).deleteById(documentId); // repository not called
    }

    @Test
    void saveDocument_ShouldThrowException_WhenValidationFails() {
        // Arrange
        DocumentDTO invalidDocumentDTO = new DocumentDTO();

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            documentService.saveDocument(invalidDocumentDTO, file);
        });
        assertTrue(exception.getMessage().contains("Validation failed for Document"));
    }

    @Test
    void getDocumentsByName_ShouldReturnPaginatedDocumentDTOs() {
        // Arrange
        Document document = new Document();
        document.setId(1L);
        document.setName("Doc 1");

        Page<Document> documentPage = new PageImpl<>(List.of(document), PageRequest.of(0, 10), 1);

        when(documentRepository.findByNameContainingIgnoreCase("Doc", PageRequest.of(0, 10)))
                .thenReturn(documentPage);

        // Act
        Page<DocumentDTO> resultPage = documentService.getDocumentsByName("Doc", PageRequest.of(0, 10));

        // Assert
        Assertions.assertEquals(1, resultPage.getTotalElements());
        assertEquals("Doc 1", resultPage.getContent().get(0).getName());
    }
}
