package org.example.paperless_services.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "storage")
public class StorageConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}

