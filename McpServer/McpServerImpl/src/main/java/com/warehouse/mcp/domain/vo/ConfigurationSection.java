package com.warehouse.mcp.domain.vo;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import com.warehouse.mcp.domain.exception.McpInvalidRequestException;

public enum ConfigurationSection {
    SHIPMENT, DEPARTMENTS, FLEET, COMMUNICATION, REGIONAL, SORTING, PICKUP_POINTS;

    public static ConfigurationSection from(final String value) {
        if (value == null || value.isBlank()) {
            throw new McpInvalidRequestException("Parameter section is required");
        }
        final String normalized = value.trim().toUpperCase(Locale.ROOT).replace('-', '_');
        return Arrays.stream(values())
                .filter(section -> section.name().equals(normalized))
                .findFirst()
                .orElseThrow(() -> new McpInvalidRequestException("Unknown configuration section " + value
                        + ". Use one of: " + Arrays.stream(values()).map(Enum::name)
                        .collect(Collectors.joining(", "))));
    }
}
