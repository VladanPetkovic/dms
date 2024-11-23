package org.example.paperless_rest.exception;

public class DocumentValidationException extends RuntimeException {
    public DocumentValidationException(String message) {
        super(message);
    }
}
