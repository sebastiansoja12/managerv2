package com.warehouse.terminal.domain.model.exception;

import com.warehouse.commonassets.identificator.UserId;
import org.springframework.web.client.RestClientException;

public class UserNotFoundException extends RestClientException {

    private static final String message = "User %s not found";

    public UserNotFoundException(final UserId userId) {
        super(String.format(message, userId));
    }
}
