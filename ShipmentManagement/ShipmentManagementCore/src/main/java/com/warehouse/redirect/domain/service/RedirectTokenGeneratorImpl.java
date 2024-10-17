package com.warehouse.redirect.domain.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RedirectTokenGeneratorImpl implements RedirectTokenGenerator {

    public static final int CODE_LENGTH = 8;

    public String generateToken(Long parcelId, String email) {
        final UUID uuid = UUID.randomUUID();

        byte[] uuidBytes = asBytes(uuid, parcelId, email);

        final String hashed = hashBytes(uuidBytes);

        final String cleaned = replaceWithRandomDigits(hashed);

        return pickRandomDigits(cleaned);
    }

    private String pickRandomDigits(String cleaned) {
        final Random random = new Random();
        final StringBuilder result = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(cleaned.length());
            result.append(cleaned.charAt(randomIndex));
        }

        return result.toString();
    }

    private byte[] asBytes(UUID uuid, Long parcelId, String email) {
        final long msb = uuid.getMostSignificantBits();
        final long lsb = uuid.getLeastSignificantBits();
        final byte[] parcelIdBytes = parcelId.toString().getBytes();
        final byte[] emailBytes = email.getBytes();
        final byte[] buffer = new byte[16 + parcelIdBytes.length + emailBytes.length];

        for (int i = 0; i < CODE_LENGTH; i++) {
            buffer[i] = (byte) (msb >>> 8 * (7 - i));
            buffer[CODE_LENGTH + i] = (byte) (lsb >>> 8 * (7 - i));
        }

        return buffer;
    }

    private String hashBytes(byte[] bytes) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            final byte[] digest = md.digest(bytes);

            final StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                final String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found.");
        }
    }

    private String replaceWithRandomDigits(String input) {
        final Random random = new Random();
        final StringBuilder result = new StringBuilder(input.length());

        for (int i = 0; i < input.length(); i++) {
            final char character = input.charAt(i);

            if (Character.isLetter(character)) {
                final int randomDigit = random.nextInt(10);
                result.append(randomDigit);
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }
}
