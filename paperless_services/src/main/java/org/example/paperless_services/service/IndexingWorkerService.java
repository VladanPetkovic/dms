package org.example.paperless_services.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class IndexingWorkerService {

    private final ElasticsearchClient client;

    public IndexingWorkerService(ElasticsearchClient client) {
        this.client = client;
    }

    public String indexTextContent(String id, String content) {
        try {
            IndexRequest<Document> request = IndexRequest.of(builder -> builder
                    .index("documents")
                    .id(id)
                    .document(new Document(content))
            );

            IndexResponse response = client.index(request);
            return response.id();
        } catch (IOException e) {
            throw new RuntimeException("Failed to index document", e);
        }
    }

    @Setter
    @Getter
    public static class Document {
        private String content;

        public Document(String content) {
            this.content = content;
        }
    }
}