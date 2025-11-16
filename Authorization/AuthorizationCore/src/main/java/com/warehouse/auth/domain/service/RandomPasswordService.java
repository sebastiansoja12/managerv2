package com.warehouse.auth.domain.service;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Service
public class RandomPasswordService {

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_=+[]{}<>?";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generatePassword(final int length, final boolean useUpper, final boolean useDigits, final boolean useSpecial) {
        final StringBuilder characters = new StringBuilder(LOWER);
        if (useUpper) characters.append(UPPER);
        if (useDigits) characters.append(DIGITS);
        if (useSpecial) characters.append(SPECIAL);

        final StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            final char c = characters.charAt(RANDOM.nextInt(characters.length()));
            password.append(c);
        }

        return password.toString();
    }
}

