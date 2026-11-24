package com.warehouse.mcpkey.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.mcpkey.application.port.primary.command.GenerateMcpKeyCommand;
import com.warehouse.mcpkey.application.port.primary.result.GeneratedMcpKey;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;
import com.warehouse.mcpkey.domain.model.McpKey;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.GenerateMcpKeyApiRequest;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.GeneratedMcpKeyApiResponse;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.McpAccessApiRequest;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.McpAccessApiResponse;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.McpKeyApiResponse;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.McpKeyIdDto;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.RevealedMcpKeyApiResponse;
import com.warehouse.mcpkey.domain.vo.McpKeySecret;

import java.util.UUID;

public final class McpKeyApiMapper {

    private McpKeyApiMapper() {
    }

    public static RevealedMcpKeyApiResponse toResponse(final McpKeySecret secret) {
        return new RevealedMcpKeyApiResponse(secret.value());
    }

    public static McpAccessPolicy toPolicy(
            final McpAccessApiRequest request) {
        return new McpAccessPolicy(request.enabled(), request.modules());
    }

    public static McpAccessApiResponse toResponse(
            final McpAccessPolicy policy) {
        return new McpAccessApiResponse(policy.enabled(), policy.modules());
    }

    public static GenerateMcpKeyCommand toCommand(final GenerateMcpKeyApiRequest generateRequest) {
        return new GenerateMcpKeyCommand(generateRequest.name(), generateRequest.expiresAt());
    }

    public static McpKeyId toMcpKeyId(final UUID mcpKeyId) {
        return new McpKeyId(mcpKeyId);
    }

    public static GeneratedMcpKeyApiResponse toResponse(final GeneratedMcpKey generatedMcpKey) {
        return new GeneratedMcpKeyApiResponse(toResponse(generatedMcpKey.mcpKey()),
                generatedMcpKey.secret().value());
    }

    public static McpKeyApiResponse toResponse(final McpKey mcpKey) {
        return new McpKeyApiResponse(
                new McpKeyIdDto(mcpKey.getMcpKeyId().value()),
                mcpKey.getName(),
                mcpKey.getKeyPrefix(),
                mcpKey.getStatus().name(),
                mcpKey.getCreatedAt(),
                mcpKey.getExpiresAt(),
                mcpKey.getLastUsedAt(),
                mcpKey.getRevokedAt());
    }
}
