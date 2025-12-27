package com.warehouse.logistics.infrastructure.adapter.primary.exception;

import lombok.Getter;

@Getter
public class RestException extends RuntimeException {

    private final int code;

    private final String message;

    public RestException(int code, String exMessage) {
        super(exMessage);
        this.code = code;
        this.message = exMessage;
    }
}