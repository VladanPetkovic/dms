package org.example.paperless_services.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ElasticSearchServiceTest {

    @Mock
    private ElasticsearchClient elasticsearchClient;

    @InjectMocks
    private ElasticSearchService elasticSearchService;

    public ElasticSearchServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndexDocument_Success() throws Exception {
        String fileName = "testFile";
        String ocrResult = "Sample OCR Result";

        IndexResponse mockResponse = mock(IndexResponse.class);
        when(mockResponse.id()).thenReturn(fileName);
        when(elasticsearchClient.index(any(IndexRequest.class))).thenReturn(mockResponse);

        elasticSearchService.indexDocument(fileName, ocrResult);

        ArgumentCaptor<IndexRequest> requestCaptor = ArgumentCaptor.forClass(IndexRequest.class);
        verify(elasticsearchClient, times(1)).index(requestCaptor.capture());

        IndexRequest<Map<String, Object>> capturedRequest = requestCaptor.getValue();
        assertEquals("documents", capturedRequest.index());
        assertEquals(fileName, capturedRequest.id());

        Map<String, Object> expectedDocument = new HashMap<>();
        expectedDocument.put("fileName", fileName);
        expectedDocument.put("ocrResult", ocrResult);

        assertEquals(expectedDocument, capturedRequest.document());
    }

    @Test
    void testIndexDocument_ExceptionHandling() throws Exception {
        String fileName = "testFile";
        String ocrResult = "Sample OCR Result";

        doThrow(new RuntimeException("Indexing failed")).when(elasticsearchClient).index(any(IndexRequest.class));

        elasticSearchService.indexDocument(fileName, ocrResult);

        verify(elasticsearchClient, times(1)).index(any(IndexRequest.class));
    }
}
