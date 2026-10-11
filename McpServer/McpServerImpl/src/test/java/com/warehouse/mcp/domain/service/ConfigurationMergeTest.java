package com.warehouse.mcp.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.warehouse.mcp.domain.exception.McpInvalidRequestException;

class ConfigurationMergeTest {

    private final ConfigurationMerge merge = new ConfigurationMerge();

    @Test
    void shouldChangeOnlyGivenNestedFields() {
        final Map<String, Object> current = Map.of("maxVehicles", 10,
                "limits", Map.of("maxWeight", 30, "maxLength", 120));

        final Map<String, Object> merged = merge.merge(current, Map.of("limits", Map.of("maxWeight", 40)));

        assertThat(merged).containsEntry("maxVehicles", 10)
                .containsEntry("limits", Map.of("maxWeight", 40, "maxLength", 120));
    }

    @Test
    void shouldRejectUnknownField() {
        assertThatThrownBy(() -> merge.merge(Map.of("limits", Map.of("maxWeight", 30)),
                Map.of("limits", Map.of("maxHeight", 1))))
                .isInstanceOf(McpInvalidRequestException.class)
                .hasMessage("Unknown configuration field limits.maxHeight");
    }

    @Test
    void shouldRequireChanges() {
        assertThatThrownBy(() -> merge.merge(Map.of("a", 1), Map.of()))
                .isInstanceOf(McpInvalidRequestException.class);
    }
}
