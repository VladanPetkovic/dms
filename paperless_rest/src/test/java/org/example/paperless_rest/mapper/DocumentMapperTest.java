package org.example.paperless_rest.mapper;

import org.example.paperless_rest.dto.DocumentDTO;
import org.example.paperless_rest.model.Document;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DocumentMapperTest {
    private final DocumentMapper mapper = DocumentMapper.INSTANCE;

    @Test
    public void givenDocumentEntity_whenMappedToDocumentDTO_thenFieldsAreMappedCorrectly() {
        // Arrange
        Document document = new Document();
        document.setId(1L);
        document.setName("Test Document");
        document.setDescription("This is a test document");
        document.setPath("/path/to/document");
        document.setType("application/pdf");
        document.setCreated_at(LocalDateTime.of(2023, 10, 1, 12, 0));
        document.setUpdated_at(LocalDateTime.of(2023, 10, 2, 12, 0));

        // Act
        DocumentDTO documentDTO = mapper.toDocumentDTO(document);

        // Assert
        assertEquals(document.getId(), documentDTO.getId());
        assertEquals(document.getName(), documentDTO.getName());
        assertEquals(document.getDescription(), documentDTO.getDescription());
        assertEquals(document.getPath(), documentDTO.getPath());
        assertEquals(document.getType(), documentDTO.getType());
        assertEquals(document.getCreated_at(), documentDTO.getCreatedAt());
        assertEquals(document.getUpdated_at(), documentDTO.getUpdatedAt());
    }

    @Test
    public void givenDocumentDTO_whenMappedToDocumentEntity_thenFieldsAreMappedCorrectly() {
        // Arrange
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(1L);
        documentDTO.setName("Test Document");
        documentDTO.setDescription("This is a test document");
        documentDTO.setPath("/path/to/document");
        documentDTO.setType("application/pdf");
        documentDTO.setCreatedAt(LocalDateTime.of(2023, 10, 1, 12, 0));
        documentDTO.setUpdatedAt(LocalDateTime.of(2023, 10, 2, 12, 0));

        // Act
        Document document = mapper.toDocument(documentDTO);

        // Assert
        assertEquals(documentDTO.getId(), document.getId());
        assertEquals(documentDTO.getName(), document.getName());
        assertEquals(documentDTO.getDescription(), document.getDescription());
        assertEquals(documentDTO.getPath(), document.getPath());
        assertEquals(documentDTO.getType(), document.getType());
        assertEquals(documentDTO.getCreatedAt(), document.getCreated_at());
        assertEquals(documentDTO.getUpdatedAt(), document.getUpdated_at());
    }

    @Test
    public void givenNullDocumentEntity_whenMappedToDocumentDTO_thenResultIsNull() {
        // Arrange
        Document document = null;

        // Act
        DocumentDTO documentDTO = mapper.toDocumentDTO(document);

        // Assert
        assertNull(documentDTO);
    }

    @Test
    public void givenNullDocumentDTO_whenMappedToDocumentEntity_thenResultIsNull() {
        // Arrange
        DocumentDTO documentDTO = null;

        // Act
        Document document = mapper.toDocument(documentDTO);

        // Assert
        assertNull(document);
    }
}
