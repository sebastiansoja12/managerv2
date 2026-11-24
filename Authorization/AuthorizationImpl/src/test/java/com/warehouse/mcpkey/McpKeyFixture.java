package com.warehouse.mcpkey;

import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.domain.enumeration.McpKeyStatus;
import com.warehouse.mcpkey.domain.model.McpKey;
import com.warehouse.mcpkey.domain.vo.McpKeySecret;
import com.warehouse.mcpkey.infrastructure.adapter.secondary.entity.McpKeyEntity;

import java.time.Instant;
import java.util.UUID;

public final class McpKeyFixture {

    public static final Instant CREATED_AT = Instant.parse("2026-11-23T10:00:00Z");
    public static final UserId OWNER_ID = new UserId(5L);
    public static final OperatorId OPERATOR_ID = OperatorId.of(20L);
    public static final McpKeySecret SECRET = new McpKeySecret("mcp_fixture-secret-value");

    private McpKeyFixture() {
    }

    public static McpKeyEntity entity() {
        final McpKey key = activeKey();
        final McpKeyEntity entity = new McpKeyEntity(key.getMcpKeyId(), OWNER_ID, "Assistant", SECRET.hash(),
                SECRET.visiblePrefix(), McpKeyStatus.ACTIVE, CREATED_AT, null, null, null);
        entity.assignOperator(OPERATOR_ID);
        return entity;
    }

    public static McpKey activeKey() {
        return key(McpKeyStatus.ACTIVE, null);
    }

    public static McpKey keyExpiringAt(final Instant expiresAt) {
        return key(McpKeyStatus.ACTIVE, expiresAt);
    }

    public static McpKey revokedKey() {
        return key(McpKeyStatus.REVOKED, null);
    }

    private static McpKey key(final McpKeyStatus status, final Instant expiresAt) {
        return new McpKey(new McpKeyId(UUID.fromString("7f2c1d7e-2a6b-4c7a-9a77-2d1e0e3f9b11")), OWNER_ID,
                "Claude Code", SECRET.hash(), SECRET.visiblePrefix(), CREATED_AT, expiresAt, null,
                McpKeyStatus.REVOKED.equals(status) ? CREATED_AT : null, status, OPERATOR_ID);
    }
}
