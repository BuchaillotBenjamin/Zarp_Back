package org.example.zarp_back.config.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Recurso no encontrado.");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
