package com.warehouse.mcp.application.port.primary.result;

import java.util.Map;

import com.warehouse.mcp.domain.vo.ConfigurationSection;

public record ConfigurationView(ConfigurationSection section, Map<String, Object> configuration) {
}
