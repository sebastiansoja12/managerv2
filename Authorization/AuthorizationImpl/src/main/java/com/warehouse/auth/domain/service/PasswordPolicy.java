package com.warehouse.auth.domain.service;

import java.nio.charset.StandardCharsets;

public class PasswordPolicy {

    public static final int MIN_LENGTH = 8;

    /** BCrypt ignores everything after 72 bytes. */
    public static final int MAX_BYTES = 72;

    public boolean isAcceptable(final String password, final String username) {
        return password != null
                && !password.isBlank()
                && password.length() >= MIN_LENGTH
                && password.getBytes(StandardCharsets.UTF_8).length <= MAX_BYTES
                && !password.equalsIgnoreCase(username);
    }
}
