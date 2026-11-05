package com.warehouse.mcp.domain.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.warehouse.mcp.domain.exception.McpInvalidRequestException;

public class ConfigurationMerge {

    public Map<String, Object> merge(final Map<String, Object> current, final Map<String, Object> changes) {
        if (changes == null || changes.isEmpty()) {
            throw new McpInvalidRequestException("Configuration changes are required");
        }
        return merge(current, changes, "");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> merge(final Map<String, Object> current, final Map<String, Object> changes,
                                     final String path) {
        final Map<String, Object> merged = new LinkedHashMap<>(current);
        changes.forEach((key, value) -> {
            final String fieldPath = path.isEmpty() ? key : path + "." + key;
            if (!current.containsKey(key)) {
                throw new McpInvalidRequestException("Unknown configuration field " + fieldPath);
            }
            final Object currentValue = current.get(key);
            if (currentValue instanceof Map<?, ?> currentMap && value instanceof Map<?, ?> valueMap) {
                merged.put(key, merge((Map<String, Object>) currentMap, (Map<String, Object>) valueMap, fieldPath));
            } else {
                merged.put(key, value);
            }
        });
        return merged;
    }
}
