package com.warehouse.geocoding.infrastructure.adapter.secondary;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class GeocodingPasswordCipher {

    private static final int IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final String encryptionKey;

    public GeocodingPasswordCipher(final String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String encrypt(final String password) {
        if (password == null) {
            return null;
        }
        validateEncryptionKey();
        try {
            final byte[] iv = new byte[IV_LENGTH];
            SECURE_RANDOM.nextBytes(iv);

            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey(), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            final byte[] encryptedPassword = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));

            final ByteBuffer payload = ByteBuffer.allocate(iv.length + encryptedPassword.length);
            payload.put(iv);
            payload.put(encryptedPassword);
            return Base64.getEncoder().encodeToString(payload.array());
        } catch (final GeneralSecurityException exception) {
            throw new IllegalStateException("Unable to encrypt geocoding API password", exception);
        }
    }

    public String decrypt(final String encryptedPassword) {
        if (encryptedPassword == null) {
            return null;
        }
        validateEncryptionKey();
        try {
            final byte[] payload = Base64.getDecoder().decode(encryptedPassword);
            final ByteBuffer buffer = ByteBuffer.wrap(payload);
            final byte[] iv = new byte[IV_LENGTH];
            buffer.get(iv);
            final byte[] password = new byte[buffer.remaining()];
            buffer.get(password);

            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey(), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            return new String(cipher.doFinal(password), StandardCharsets.UTF_8);
        } catch (final GeneralSecurityException | IllegalArgumentException exception) {
            throw new IllegalStateException("Unable to decrypt geocoding API password", exception);
        }
    }

    private SecretKeySpec secretKey() {
        try {
            final byte[] key = MessageDigest.getInstance("SHA-256")
                    .digest(encryptionKey.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(key, "AES");
        } catch (final NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algorithm not available", exception);
        }
    }

    private void validateEncryptionKey() {
        if (encryptionKey == null || encryptionKey.isBlank()) {
            throw new IllegalStateException("Missing geocoding encryption key configuration");
        }
    }
}
