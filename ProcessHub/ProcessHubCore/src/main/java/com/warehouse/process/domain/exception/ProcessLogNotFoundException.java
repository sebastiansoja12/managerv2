package com.warehouse.process.domain.exception;

public class ProcessLogNotFoundException extends RuntimeException {
    public ProcessLogNotFoundException(final String message) {
        super(message);
    }
}
