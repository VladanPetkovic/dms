package org.example.paperless_services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import org.example.paperless_services.service.IndexingWorkerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class IndexingWorkerServiceTest {

    private ElasticsearchClient mockClient;
    private IndexingWorkerService service;

    @BeforeEach
    public void setup() {
        // Mock ElasticsearchClient
        mockClient = Mockito.mock(ElasticsearchClient.class);
        service = new IndexingWorkerService(mockClient); // Inject mocked client
    }

    @Test
    public void testIndexTextContent() throws Exception {
        // Mock IndexResponse
        IndexResponse mockResponse = mock(IndexResponse.class);
        when(mockResponse.id()).thenReturn("test-id");

        // Mock client.index() to return the mock response
        when(mockClient.index(any(IndexRequest.class))).thenReturn(mockResponse);

        // Test the method
        String id = service.indexTextContent("1", "Sample Content");
        assertEquals("test-id", id, "Indexing should return the correct ID");

        // Verify interactions
        verify(mockClient, times(1)).index(any(IndexRequest.class));
    }
}