package org.example.paperless_services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.example.paperless_services.config.ElasticSearchConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ElasticSearchConfigTest {

    @Test
    public void testCreateClient() {
        ElasticSearchConfig config = new ElasticSearchConfig();
        ElasticsearchClient client = config.createClient();

        // Ensure the client is not null
        assertNotNull(client, "Elasticsearch client should not be null");
    }
}