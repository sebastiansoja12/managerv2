package com.warehouse.account.domain.vo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Objects;

public record InvitationToken(String value) {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TOKEN_BYTES = 32;

    public InvitationToken {
        Objects.requireNonNull(value, "Invitation token cannot be null");
    }

    public static InvitationToken generate() {
        final byte[] bytes = new byte[TOKEN_BYTES];
        RANDOM.nextBytes(bytes);
        return new InvitationToken(Base64.getUrlEncoder().withoutPadding().encodeToString(bytes));
    }

    public String hash() {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (final NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }
}
