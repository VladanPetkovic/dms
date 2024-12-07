package org.example.paperless_rest.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ElasticSearchService {
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    /**
     * This function returns the file-names.
     *
     * @param search   - the search-param
     * @param pageable -
     * @return List of file-names
     */
    public List<String> searchDocuments(String search, Pageable pageable) {
        try {
            SearchRequest request = SearchRequest.of(s -> s
                    .index("documents") // index name
                    .query(q -> q
                            .match(m -> m
                                    .field("ocrResult")
                                    .query(search)      // search term
                            )
                    )
                    .from((int) pageable.getOffset())
                    .size(pageable.getPageSize())
            );

            SearchResponse<Map> response = elasticsearchClient.search(request, Map.class);

            List<String> fileNames = new ArrayList<>();
            response.hits().hits().forEach(hit -> {
                Map<String, Object> source = hit.source();
                if (source != null) {
                    fileNames.add((String) source.get("fileName"));
                }
            });

            return fileNames;
        } catch (Exception e) {
            log.error("Failed to search documents", e);
            return new ArrayList<>();
        }
    }
}
