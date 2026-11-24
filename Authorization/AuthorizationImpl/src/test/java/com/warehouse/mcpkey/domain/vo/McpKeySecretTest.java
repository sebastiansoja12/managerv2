package com.warehouse.mcpkey.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class McpKeySecretTest {

    @Test
    void shouldGenerateUniquePrefixedSecrets() {
        final McpKeySecret first = McpKeySecret.generate();
        final McpKeySecret second = McpKeySecret.generate();

        assertThat(first.value()).startsWith(McpKeySecret.PREFIX);
        assertThat(first.value().length()).isGreaterThan(40);
        assertThat(first).isNotEqualTo(second);
        assertThat(first.hash()).isNotEqualTo(second.hash()).hasSize(64);
    }

    @Test
    void shouldNotExposeSecretInToString() {
        final McpKeySecret secret = McpKeySecret.generate();

        assertThat(secret.toString()).doesNotContain(secret.value());
    }

    @Test
    void shouldRecognizeMcpKeyPrefix() {
        assertThat(McpKeySecret.hasMcpKeyPrefix("mcp_abc")).isTrue();
        assertThat(McpKeySecret.hasMcpKeyPrefix("abc")).isFalse();
    }
}
