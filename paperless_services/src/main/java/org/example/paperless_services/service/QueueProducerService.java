package org.example.paperless_services.service;

import lombok.extern.slf4j.Slf4j;
import org.example.paperless_services.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueueProducerService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String fileName, String message) {
        log.info("About to send message for fileName: " + fileName);
        rabbitTemplate.convertAndSend(RabbitMqConfig.RESULT_QUEUE, fileName + ";" + message);
        log.info("Message sent successfully to RESULT_QUEUE!");
    }
}

