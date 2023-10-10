package com.warehouse.suppliertoken.domain.exception;

public class MissingParcelIdException extends RuntimeException {

    String message;

    public MissingParcelIdException(String message) {
        this.message = message;
    }
}
