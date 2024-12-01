package org.example.paperless_services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.example.paperless_services.config.ElasticsearchConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ElasticsearchConfigTest {

    @Test
    public void testCreateClient() {
        ElasticsearchConfig config = new ElasticsearchConfig();
        ElasticsearchClient client = config.createClient();

        // Ensure the client is not null
        assertNotNull(client, "Elasticsearch client should not be null");
    }
}