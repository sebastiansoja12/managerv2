package com.warehouse.voronoi.infrastructure.adapter.secondary.exception;

public class CoordinatesTechnicalException extends RuntimeException {
    public CoordinatesTechnicalException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public CoordinatesTechnicalException(String exMessage) {
        super(exMessage);
    }
}
