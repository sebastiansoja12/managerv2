package com.warehouse.chat.domain.exception;

public class ChatException extends RuntimeException {

    public enum Reason { INVALID_REQUEST, ACCESS_DENIED, NOT_FOUND, CONFLICT }

    private final Reason reason;

    public ChatException(final Reason reason, final String message) {
        super(message);
        this.reason = reason;
    }

    public Reason reason() {
        return reason;
    }
}
