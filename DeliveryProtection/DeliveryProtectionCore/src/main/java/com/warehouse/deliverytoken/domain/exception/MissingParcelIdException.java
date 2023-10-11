package com.warehouse.deliverytoken.domain.exception;

public class MissingParcelIdException extends RuntimeException {

    public MissingParcelIdException(String exMessage) {
        super(exMessage);
    }

}
