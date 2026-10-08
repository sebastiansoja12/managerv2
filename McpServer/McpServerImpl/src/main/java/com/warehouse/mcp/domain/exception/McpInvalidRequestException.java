package com.warehouse.mcp.domain.exception;

public class McpInvalidRequestException extends RuntimeException {

    public McpInvalidRequestException(final String message) {
        super(message);
    }
}
