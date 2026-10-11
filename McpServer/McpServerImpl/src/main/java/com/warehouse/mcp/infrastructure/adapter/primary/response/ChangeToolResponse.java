package com.warehouse.mcp.infrastructure.adapter.primary.response;

public record ChangeToolResponse(String result, String message) {

    public static ChangeToolResponse done(final String message) {
        return new ChangeToolResponse("DONE", message);
    }
}
