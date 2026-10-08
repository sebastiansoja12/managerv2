package com.warehouse.mcp.application.port.secondary;

import java.util.Map;

import com.warehouse.mcp.domain.vo.ConfigurationSection;

public interface ConfigurationServicePort {
    Map<String, Object> find(final ConfigurationSection section);
    Map<String, Object> update(final ConfigurationSection section, final Map<String, Object> configuration);
}
