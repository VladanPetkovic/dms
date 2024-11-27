package org.example.paperless_rest.service;

import lombok.extern.slf4j.Slf4j;
import org.example.paperless_rest.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueueConsumerService {
    @RabbitListener(queues = RabbitMqConfig.RESULT_QUEUE)
    public void receiveMessage(String message) {
        log.info("Received message from RESULT_QUEUE: " + message);
    }
}
