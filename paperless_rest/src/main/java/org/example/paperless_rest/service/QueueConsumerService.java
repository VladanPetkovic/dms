package org.example.paperless_rest.service;

import org.example.paperless_rest.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class QueueConsumerService {
    @RabbitListener(queues = RabbitMqConfig.RESULT_QUEUE)
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }
}
