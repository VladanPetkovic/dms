package org.example.paperless_rest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 10, message = "Name cannot exceed 10 characters")
    private String name;
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 100, message = "Description cannot exceed 100 characters")
    private String description;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String path;

    @Pattern(regexp = "image/png|image/jpeg|image/jpg|application/pdf",
            message = "Type must be one of: image/png, image/jpeg, image/jpg, application/pdf")
    @NotNull(message = "Type cannot be null")
    private String type;
}
