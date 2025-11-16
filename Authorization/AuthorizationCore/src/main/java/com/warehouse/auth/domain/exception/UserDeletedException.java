package com.warehouse.auth.domain.exception;

import org.springframework.web.client.RestClientException;

public class UserDeletedException extends RestClientException {
    public UserDeletedException(final String msg) {
        super(msg);
    }
}
