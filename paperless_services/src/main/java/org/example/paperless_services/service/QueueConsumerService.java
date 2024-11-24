package org.example.paperless_services.service;

import lombok.extern.slf4j.Slf4j;
import org.example.paperless_services.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueueConsumerService {
    @Autowired
    private OcrService ocrService;

    @Autowired
    private QueueProducerService queueProducerService;

    @RabbitListener(queues = RabbitMqConfig.OCR_QUEUE)
    public void receiveMessage(String fileName) {
        log.info("Received message from OCR_QUEUE: " + fileName);
        try {
            // get ocr-result
            String ocrResult = ocrService.getStringForFile(fileName);

            // sending the result back
            queueProducerService.sendMessage(ocrResult);

        } catch (Exception e) {
            System.err.println("Failed to process the file: " + fileName);
            e.printStackTrace();
        }
    }
}