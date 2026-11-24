package com.warehouse.auth;

import java.util.Map;
import java.util.Set;

public interface McpToolAccessApiService {
    Set<String> allowedTools(final Map<String, Mcp> rules);
}
