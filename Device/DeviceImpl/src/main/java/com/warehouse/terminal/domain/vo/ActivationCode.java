package com.warehouse.terminal.domain.vo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Objects;

public record ActivationCode(String value) {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 8;
    private static final int GROUP_LENGTH = 4;

    public ActivationCode {
        Objects.requireNonNull(value, "Activation code cannot be null");
    }

    public static ActivationCode generate() {
        final StringBuilder code = new StringBuilder();
        for (int index = 0; index < CODE_LENGTH; index++) {
            if (index > 0 && index % GROUP_LENGTH == 0) {
                code.append('-');
            }
            code.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new ActivationCode(code.toString());
    }

    public String hash() {
        final String normalized = value.replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(normalized.getBytes(StandardCharsets.UTF_8)));
        } catch (final NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }
}
