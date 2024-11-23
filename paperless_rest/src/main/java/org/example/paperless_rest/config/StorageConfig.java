package org.example.paperless_rest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("storage")
public class StorageConfig {
    private String location = "upload_dir";
}

