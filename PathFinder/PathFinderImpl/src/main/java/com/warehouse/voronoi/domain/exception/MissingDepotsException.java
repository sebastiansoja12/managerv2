package com.warehouse.voronoi.domain.exception;

public class MissingDepotsException extends RuntimeException {
    public MissingDepotsException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public MissingDepotsException(String exMessage) {
        super(exMessage);
    }
}
