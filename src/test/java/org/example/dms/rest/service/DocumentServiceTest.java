package org.example.dms.rest.service;

import org.example.dms.rest.dto.DocumentDTO;
import org.example.dms.rest.mapper.DocumentMapper;
import org.example.dms.rest.model.Document;
import org.example.dms.rest.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DocumentServiceTest {
    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private MultipartFile file; // Mocking MultipartFile for file upload tests

    @InjectMocks
    private DocumentService documentService;

    private final DocumentMapper mapper = DocumentMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveDocuments_ShouldSaveBatchOfDocuments() {
        // Arrange
        DocumentDTO doc1 = new DocumentDTO();
        doc1.setName("Doc 1");
        DocumentDTO doc2 = new DocumentDTO();
        doc2.setName("Doc 2");

        List<DocumentDTO> documentDTOs = List.of(doc1, doc2);
        List<Document> documents = documentDTOs.stream().map(mapper::toDocument).toList();

        // Mock repository behavior
        when(documentRepository.saveAll(anyList())).thenReturn(documents);

        // Act
        List<DocumentDTO> savedDocumentDTOs = documentService.saveDocuments(documentDTOs);

        // Assert
        assertEquals(2, savedDocumentDTOs.size());
        verify(documentRepository, times(1)).saveAll(anyList());
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
        updatedDocumentDTO.setType("pdf");

        when(documentRepository.findById(1L)).thenReturn(Optional.of(existingDocument));
        when(documentRepository.save(any(Document.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        DocumentDTO result = documentService.updateDocument(1L, updatedDocumentDTO);

        // Assert
        assertEquals("Updated", result.getName());
        verify(documentRepository, times(1)).save(any(Document.class));
    }

    @Test
    void deleteDocument_ShouldDeleteDocumentById() {
        // Arrange
        Long documentId = 1L;
        doNothing().when(documentRepository).deleteById(documentId);

        // Act
        documentService.deleteDocument(documentId);

        // Assert
        verify(documentRepository, times(1)).deleteById(documentId);
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
        assertEquals(1, resultPage.getTotalElements());
        assertEquals("Doc 1", resultPage.getContent().get(0).getName());
    }
}
