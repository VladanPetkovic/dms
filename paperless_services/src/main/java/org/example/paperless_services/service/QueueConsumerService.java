package org.example.paperless_services.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.paperless_services.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class QueueConsumerService {
    @Autowired
    private OcrService ocrService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private QueueProducerService queueProducerService;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @RabbitListener(queues = RabbitMqConfig.OCR_QUEUE)
    public void receiveMessage(String fileName) {
        log.info("Received message from OCR_QUEUE: " + fileName);
        try {
            Path filePath = storageService.loadAndSave(fileName);

            // get ocr-result
            String ocrResult = ocrService.processFile(filePath);

            // 6a: sending the result back
            queueProducerService.sendMessage(ocrResult);

            // 6b: index document
            indexDocument(fileName, ocrResult);
        } catch (Exception e) {
            System.err.println("Failed to process the file: " + fileName);
            e.printStackTrace();
        }
    }

    private void indexDocument(String fileName, String ocrResult) {
        try {
            Map<String, Object> document = new HashMap<>();
            document.put("fileName", fileName);
            document.put("ocrResult", ocrResult);

            // Index the document
            IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                    .index("documents") // The index name
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