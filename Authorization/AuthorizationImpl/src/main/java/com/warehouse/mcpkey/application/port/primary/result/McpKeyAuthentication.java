package com.warehouse.mcpkey.application.port.primary.result;

import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;

public record McpKeyAuthentication(McpKeyId mcpKeyId, UserId ownerId, OperatorId operatorId, Instant issuedAt) {
}
