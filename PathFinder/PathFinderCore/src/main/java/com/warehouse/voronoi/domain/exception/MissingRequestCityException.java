package com.warehouse.voronoi.domain.exception;

public class MissingRequestCityException extends RuntimeException {
    public MissingRequestCityException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public MissingRequestCityException(String exMessage) {
        super(exMessage);
    }
}