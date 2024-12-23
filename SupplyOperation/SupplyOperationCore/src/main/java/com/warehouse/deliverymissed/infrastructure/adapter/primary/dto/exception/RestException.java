package com.warehouse.deliverymissed.infrastructure.adapter.primary.dto.exception;

import lombok.Getter;

@Getter
public class RestException extends RuntimeException {

    int code;

    String message;

    public RestException(int code, String exMessage) {
        super(exMessage);
        this.code = code;
        this.message = exMessage;
    }
}