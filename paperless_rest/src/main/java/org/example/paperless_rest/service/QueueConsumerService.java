package org.example.paperless_rest.service;

import lombok.extern.slf4j.Slf4j;
import org.example.paperless_rest.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueueConsumerService {
    @Autowired
    private DocumentService documentService;

    @RabbitListener(queues = RabbitMqConfig.RESULT_QUEUE)
    public void receiveMessage(String message) {
        log.info("Received message from RESULT_QUEUE: " + message);
        if (message == null) {
            log.error("Received null or empty message from RESULT_QUEUE.");
            return;
        }

        String[] parts = message.split(";", 2);
        if (parts.length == 2) {
            String fileName = parts[0];
            String content = parts[1];

            content = content.length() > 1000 ? content.substring(0, 1000) : content;

            log.info("Processing fileName: " + fileName + ", content length: " + content.length());
            documentService.addContent(fileName, content);
        } else {
            log.error("Invalid message format. Message: " + message);
        }
    }
}
