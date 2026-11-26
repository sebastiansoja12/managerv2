package com.warehouse.mcpkey.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.McpKeyFixture;
import com.warehouse.mcpkey.domain.enumeration.McpKeyStatus;

class McpKeyTest {

    private static final Instant NOW = McpKeyFixture.CREATED_AT.plusSeconds(3600);

    @Test
    void shouldBeActiveWhenNotRevokedAndNotExpired() {
        final McpKey mcpKey = McpKeyFixture.keyExpiringAt(NOW.plusSeconds(60));

        assertThat(mcpKey.isActiveAt(NOW)).isTrue();
    }

    @Test
    void shouldNotBeActiveWhenExpired() {
        final McpKey mcpKey = McpKeyFixture.keyExpiringAt(NOW);

        assertThat(mcpKey.isActiveAt(NOW)).isFalse();
    }

    @Test
    void shouldNotBeActiveAfterRevocation() {
        final McpKey mcpKey = McpKeyFixture.activeKey();

        mcpKey.revoke(NOW);

        assertThat(mcpKey.isActiveAt(NOW)).isFalse();
        assertThat(mcpKey.getStatus()).isEqualTo(McpKeyStatus.REVOKED);
        assertThat(mcpKey.getRevokedAt()).isEqualTo(NOW);
    }

    @Test
    void shouldKeepFirstRevocationDateWhenRevokedTwice() {
        final McpKey mcpKey = McpKeyFixture.activeKey();

        mcpKey.revoke(NOW);
        mcpKey.revoke(NOW.plusSeconds(60));

        assertThat(mcpKey.getRevokedAt()).isEqualTo(NOW);
    }

    @Test
    void shouldRecordUsageAtMostOncePerMinute() {
        final McpKey mcpKey = McpKeyFixture.activeKey();

        mcpKey.recordUsage(NOW);
        mcpKey.recordUsage(NOW.plusSeconds(30));

        assertThat(mcpKey.getLastUsedAt()).isEqualTo(NOW);

        mcpKey.recordUsage(NOW.plusSeconds(60));

        assertThat(mcpKey.getLastUsedAt()).isEqualTo(NOW.plusSeconds(60));
    }

    @Test
    void shouldRecognizeOwner() {
        final McpKey mcpKey = McpKeyFixture.activeKey();

        assertThat(mcpKey.isOwnedBy(McpKeyFixture.OWNER_ID)).isTrue();
        assertThat(mcpKey.isOwnedBy(new UserId(6L))).isFalse();
    }
}
