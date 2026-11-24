package com.warehouse.mcpkey.application.port.primary.command;

import java.time.Instant;

public record GenerateMcpKeyCommand(String name, Instant expiresAt) {
}
