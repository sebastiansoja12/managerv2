package com.warehouse.mcp.infrastructure.adapter.primary;

public class McpToolFailure extends RuntimeException {

    public McpToolFailure(final String message) {
        super(message);
    }
}
