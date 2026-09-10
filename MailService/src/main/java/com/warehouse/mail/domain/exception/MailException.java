package com.warehouse.mail.domain.exception;

public class MailException extends RuntimeException {

    public MailException(final String message) {
        super(message);
    }
}
