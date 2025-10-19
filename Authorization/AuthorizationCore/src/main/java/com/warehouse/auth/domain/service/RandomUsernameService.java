package com.warehouse.auth.domain.service;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Service
public class RandomUsernameService {

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateUsername(final int minLetters, final int maxLetters, final boolean withDigits) {
        final int lettersCount = RANDOM.nextInt(maxLetters - minLetters + 1) + minLetters;
        final StringBuilder username = new StringBuilder();

        for (int i = 0; i < lettersCount; i++) {
            final char c = LETTERS.charAt(RANDOM.nextInt(LETTERS.length()));
            username.append(c);
        }

        if (withDigits) {
            final int digitsCount = RANDOM.nextInt(3) + 1;
            for (int i = 0; i < digitsCount; i++) {
                final char d = DIGITS.charAt(RANDOM.nextInt(DIGITS.length()));
                username.append(d);
            }
        }

        return username.toString();
    }
}

