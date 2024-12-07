package org.example.paperless_services.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ElasticSearchService {
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public void indexDocument(String fileName, String ocrResult) {
        try {
            Map<String, Object> document = new HashMap<>();
            document.put("fileName", fileName);
            document.put("ocrResult", ocrResult);

            IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                    .index("documents") // index name
                    .id(fileName)      // UUID is our id
                    .document(document)
            );

            IndexResponse response = elasticsearchClient.index(request);
            log.info("Document indexed successfully: " + response.id());
        } catch (Exception e) {
            log.error("Failed to index document with fileName: " + fileName, e);
        }
    }
}
