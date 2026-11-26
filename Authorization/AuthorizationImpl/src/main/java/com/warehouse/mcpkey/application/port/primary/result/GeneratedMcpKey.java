package com.warehouse.mcpkey.application.port.primary.result;

import com.warehouse.mcpkey.domain.model.McpKey;
import com.warehouse.mcpkey.domain.vo.McpKeySecret;

public record GeneratedMcpKey(McpKey mcpKey, McpKeySecret secret) {
}
