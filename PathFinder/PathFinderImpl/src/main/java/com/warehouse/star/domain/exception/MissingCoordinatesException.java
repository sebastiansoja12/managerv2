package com.warehouse.star.domain.exception;

public class MissingCoordinatesException extends RuntimeException {
    public MissingCoordinatesException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public MissingCoordinatesException(String exMessage) {
        super(exMessage);
    }
}
