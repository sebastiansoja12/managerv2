package com.warehouse.billing.api.exception;

public class PriceListNotFoundApiException extends RuntimeException {

    public PriceListNotFoundApiException(final String message) {
        super(message);
    }
}
