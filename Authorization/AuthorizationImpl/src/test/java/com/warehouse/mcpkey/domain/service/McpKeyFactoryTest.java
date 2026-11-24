package com.warehouse.mcpkey.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import com.warehouse.mcpkey.McpKeyFixture;
import com.warehouse.mcpkey.domain.enumeration.McpKeyStatus;
import com.warehouse.mcpkey.domain.exception.InvalidMcpKeyException;
import com.warehouse.mcpkey.domain.model.McpKey;
import com.warehouse.mcpkey.domain.vo.McpKeySecret;

class McpKeyFactoryTest {

    private static final Instant NOW = McpKeyFixture.CREATED_AT;

    private final McpKeyFactory factory = new McpKeyFactory(Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void shouldCreateActiveKeyStoringOnlyHash() {
        final McpKeySecret secret = McpKeySecret.generate();

        final McpKey mcpKey = factory.create(McpKeyFixture.OWNER_ID, "  Claude Code  ", secret, NOW.plusSeconds(60));

        assertThat(mcpKey.getName()).isEqualTo("Claude Code");
        assertThat(mcpKey.getStatus()).isEqualTo(McpKeyStatus.ACTIVE);
        assertThat(mcpKey.getCreatedAt()).isEqualTo(NOW);
        assertThat(mcpKey.getKeyHash()).isEqualTo(secret.hash()).isNotEqualTo(secret.value());
        assertThat(mcpKey.getKeyPrefix()).startsWith(McpKeySecret.PREFIX);
        assertThat(secret.value()).startsWith(mcpKey.getKeyPrefix());
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThatThrownBy(() -> factory.create(McpKeyFixture.OWNER_ID, " ", McpKeySecret.generate(), null))
                .isInstanceOf(InvalidMcpKeyException.class);
    }

    @Test
    void shouldThrowExceptionWhenNameIsTooLong() {
        final String name = "a".repeat(McpKeyFactory.NAME_MAX_LENGTH + 1);

        assertThatThrownBy(() -> factory.create(McpKeyFixture.OWNER_ID, name, McpKeySecret.generate(), null))
                .isInstanceOf(InvalidMcpKeyException.class);
    }

    @Test
    void shouldThrowExceptionWhenExpirationIsNotInFuture() {
        assertThatThrownBy(() -> factory.create(McpKeyFixture.OWNER_ID, "Claude Code", McpKeySecret.generate(), NOW))
                .isInstanceOf(InvalidMcpKeyException.class);
    }
}
