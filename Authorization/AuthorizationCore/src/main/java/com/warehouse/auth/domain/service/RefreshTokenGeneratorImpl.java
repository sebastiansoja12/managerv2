package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.provider.RefreshTokenProvider;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@AllArgsConstructor
public class RefreshTokenGeneratorImpl implements RefreshTokenGenerator {

    @NonNull
    private final RefreshTokenProvider refreshTokenProvider;

    @Override
    public String generateToken(User user) {
        final String input = user.getUsername() +
                user.getPassword() +
                user.getFirstName() +
                user.getLastName() +
                user.getEmail() +
                user.getRole().toString() +
                user.getDepotCode() +
                refreshTokenProvider.getKey();

        String token = generateSHA256Hash(input);

        final String randomPart = UUID.randomUUID().toString().replace("-", "");

        return token + randomPart;
    }

    private String generateSHA256Hash(String input) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hashBytes = digest.digest(input.getBytes());

            final StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                final String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available.", e);
        }
    }
}
