package org.example.paperless_services.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

@Configuration
public class RabbitMqConfig {
    public static final String OCR_QUEUE = "OCR_QUEUE";
    public static final String RESULT_QUEUE = "RESULT_QUEUE";

    @Bean
    public Queue ocr_queue() {
        return new Queue(OCR_QUEUE, false);
    }

    @Bean
    public Queue result_queue() {
        return new Queue(RESULT_QUEUE, false);
    }
}
