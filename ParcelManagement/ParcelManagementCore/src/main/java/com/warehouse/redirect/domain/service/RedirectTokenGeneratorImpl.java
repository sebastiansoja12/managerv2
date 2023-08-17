package com.warehouse.redirect.domain.service;

import lombok.AllArgsConstructor;

import java.security.SecureRandom;

/**
 * // TODO INPL-5246
 */
@AllArgsConstructor
public class RedirectTokenGeneratorImpl implements RedirectTokenGenerator {
    private static final int TOKEN_LENGTH = 8;

    @Override
    public String generateToken(Long parcelId, String email) {
        final SecureRandom secureRandom = new SecureRandom();

        final long minValue = 10000000L;
        final long maxValue = 99999999L;
        final long range = maxValue - minValue + 1;
        final long generatedNumber = secureRandom.nextLong() % range + minValue;

        return String.format("%08d", generatedNumber);
    }
}
