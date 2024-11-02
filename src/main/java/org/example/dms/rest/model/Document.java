package org.example.dms.rest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private LocalDateTime created_at = LocalDateTime.now();
    private LocalDateTime updated_at;
    private String path;

    // File content as binary data
    @Lob
    private byte[] data;

    // (e.g., "application/pdf", "text/plain")
    private String type;
}
