package com.warehouse.returning.domain.enumeration;

import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    ACCEPTED, NOT_FOUND, BAD_REQUEST, INTERNAL_SERVER_ERROR;

    public static ErrorCode of(final HttpStatusCode statusCode) {
        return statusCode.is4xxClientError() ? BAD_REQUEST : statusCode.is5xxServerError() ? INTERNAL_SERVER_ERROR : NOT_FOUND;
    }
}
