package org.example.dms.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Match all API endpoints
                .allowedOrigins("http://localhost:80", "http://localhost:8080", "http://localhost:8081") // Allow multiple origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowCredentials(true) // Allow credentials (if needed)
                .allowedHeaders("*"); // Allow all headers
    }
}
