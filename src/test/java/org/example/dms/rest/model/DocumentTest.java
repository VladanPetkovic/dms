package org.example.dms.rest.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DocumentTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void whenNameIsBlank_thenValidationFails() {
        Document document = new Document();
        document.setName("");  // Invalid blank name
        document.setDescription("Valid Description");
        document.setType("application/pdf");

        Set<ConstraintViolation<Document>> violations = validator.validate(document);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Name cannot be blank")));
    }

    @Test
    public void whenNameExceedsMaxLength_thenValidationFails() {
        Document document = new Document();
        document.setName("ThisNameIsWayTooLong");  // invalid name length
        document.setDescription("Valid Description");
        document.setType("application/pdf");

        Set<ConstraintViolation<Document>> violations = validator.validate(document);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Name cannot exceed 10 characters")));
    }

    @Test
    public void whenDescriptionIsBlank_thenValidationFails() {
        Document document = new Document();
        document.setName("ValidName");
        document.setDescription("");  // invalid blank description
        document.setType("application/pdf");

        Set<ConstraintViolation<Document>> violations = validator.validate(document);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Description cannot be blank")));
    }

    @Test
    public void whenDescriptionExceedsMaxLength_thenValidationFails() {
        Document document = new Document();
        document.setName("ValidName");
        document.setDescription("A".repeat(101));  // invalid description length
        document.setType("application/pdf");

        Set<ConstraintViolation<Document>> violations = validator.validate(document);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Description cannot exceed 100 characters")));
    }

    @Test
    public void whenTypeIsNull_thenValidationFails() {
        Document document = new Document();
        document.setName("ValidName");
        document.setDescription("Valid Description");
        document.setType(null);  // invalid null type

        Set<ConstraintViolation<Document>> violations = validator.validate(document);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Type cannot be null")));
    }

    @Test
    public void whenDocumentIsValid_thenNoValidationErrors() {
        Document document = new Document();
        document.setName("ValidName");
        document.setDescription("Valid Description");
        document.setType("application/pdf");

        Set<ConstraintViolation<Document>> violations = validator.validate(document);

        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }
}