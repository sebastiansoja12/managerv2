package com.warehouse.mcpkey.domain.vo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Objects;

public record McpKeySecret(String value) {

    public static final String PREFIX = "mcp_";

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SECRET_BYTES = 32;
    private static final int VISIBLE_PREFIX_LENGTH = PREFIX.length() + 6;

    public McpKeySecret {
        Objects.requireNonNull(value, "MCP key cannot be null");
    }

    public static McpKeySecret generate() {
        final byte[] bytes = new byte[SECRET_BYTES];
        RANDOM.nextBytes(bytes);
        return new McpKeySecret(PREFIX + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes));
    }

    public static boolean hasMcpKeyPrefix(final String credential) {
        return credential.startsWith(PREFIX);
    }

    public String hash() {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (final NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }

    public String visiblePrefix() {
        return value.substring(0, Math.min(VISIBLE_PREFIX_LENGTH, value.length()));
    }

    @Override
    public String toString() {
        return "McpKeySecret[" + visiblePrefix() + "...]";
    }
}
