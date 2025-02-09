package com.warehouse.terminal.domain.exception;

import org.springframework.web.client.RestClientException;

public class UserNotFoundException extends RestClientException {

    private static final String message = "User %s not found";

    public UserNotFoundException(final Object value) {
        super(String.format(message, value));
    }
}
