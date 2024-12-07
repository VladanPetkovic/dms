package org.example.paperless_services.service;

import lombok.extern.slf4j.Slf4j;
import org.example.paperless_services.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@Slf4j
public class QueueConsumerService {
    @Autowired
    private OcrService ocrService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private QueueProducerService queueProducerService;

    @Autowired ElasticSearchService elasticSearchService;

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
            elasticSearchService.indexDocument(fileName, ocrResult);
        } catch (Exception e) {
            System.err.println("Failed to process the file: " + fileName);
            e.printStackTrace();
        }
    }
}