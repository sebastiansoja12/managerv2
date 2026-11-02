package com.warehouse.mcp.infrastructure.adapter.primary;

import java.util.function.Supplier;

import com.warehouse.mcp.domain.exception.McpInvalidRequestException;
import com.warehouse.mcp.domain.exception.McpToolAccessException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class McpToolExecutor {

    private static final String REQUEST_FAILED =
            "The request could not be completed. The resource may not exist or you may not have access to it.";

    public <T> T execute(final String toolName, final Supplier<T> operation) {
        log.info("MCP tool {} invoked", toolName);
        try {
            return operation.get();
        } catch (final McpToolAccessException | McpInvalidRequestException | McpToolFailure exception) {
            log.warn("MCP tool {} rejected: {}", toolName, exception.getMessage());
            throw new McpToolFailure(exception.getMessage());
        } catch (final IllegalArgumentException exception) {
            log.warn("MCP tool {} rejected invalid input: {}", toolName, exception.getMessage());
            throw new McpToolFailure(exception.getMessage() != null ? exception.getMessage() : REQUEST_FAILED);
        } catch (final RuntimeException exception) {
            log.warn("MCP tool {} failed with {}", toolName, exception.getClass().getSimpleName());
            throw new McpToolFailure(REQUEST_FAILED);
        }
    }
}
