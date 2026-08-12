package com.warehouse.commonassets.security;

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

public class CredentialCipher {

    private static final int IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final String encryptionKey;

    public CredentialCipher(final String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String encrypt(final String credential) {
        if (credential == null) {
            return null;
        }
        validateEncryptionKey();
        try {
            final byte[] initializationVector = new byte[IV_LENGTH];
            SECURE_RANDOM.nextBytes(initializationVector);

            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey(),
                    new GCMParameterSpec(GCM_TAG_LENGTH, initializationVector));
            final byte[] encryptedCredential = cipher.doFinal(credential.getBytes(StandardCharsets.UTF_8));

            final ByteBuffer payload = ByteBuffer.allocate(initializationVector.length + encryptedCredential.length);
            payload.put(initializationVector);
            payload.put(encryptedCredential);
            return Base64.getEncoder().encodeToString(payload.array());
        } catch (final GeneralSecurityException exception) {
            throw new IllegalStateException("Unable to encrypt credential", exception);
        }
    }

    public String decrypt(final String encryptedCredential) {
        if (encryptedCredential == null) {
            return null;
        }
        validateEncryptionKey();
        try {
            final byte[] payload = Base64.getDecoder().decode(encryptedCredential);
            final ByteBuffer buffer = ByteBuffer.wrap(payload);
            final byte[] initializationVector = new byte[IV_LENGTH];
            buffer.get(initializationVector);
            final byte[] credential = new byte[buffer.remaining()];
            buffer.get(credential);

            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey(),
                    new GCMParameterSpec(GCM_TAG_LENGTH, initializationVector));
            return new String(cipher.doFinal(credential), StandardCharsets.UTF_8);
        } catch (final GeneralSecurityException | IllegalArgumentException exception) {
            throw new IllegalStateException("Unable to decrypt credential", exception);
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
            throw new IllegalStateException("Missing credential encryption key configuration");
        }
    }
}
