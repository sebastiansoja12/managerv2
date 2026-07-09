package com.warehouse.terminal.domain.service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.commonassets.identificator.DeviceId;

public class DevicePairKeyService {

    private static final int IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final String pairKeySecret;

    public DevicePairKeyService(final String pairKeySecret) {
        this.pairKeySecret = pairKeySecret;
    }

    public String generateEncryptedPairKey(final DeviceId deviceId, final Instant now) {
        if (StringUtils.isBlank(pairKeySecret)) {
            throw new IllegalStateException("Missing pair key secret configuration");
        }
        try {
            final byte[] iv = new byte[IV_LENGTH];
            SECURE_RANDOM.nextBytes(iv);

            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(secretKey(), "AES"),
                    new GCMParameterSpec(GCM_TAG_LENGTH, iv));

            final String rawKey = deviceId.value() + ":" + now.toEpochMilli() + ":" + UUID.randomUUID();
            final byte[] encrypted = cipher.doFinal(rawKey.getBytes(StandardCharsets.UTF_8));
            final ByteBuffer token = ByteBuffer.allocate(iv.length + encrypted.length);
            token.put(iv);
            token.put(encrypted);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(token.array());
        } catch (final GeneralSecurityException ex) {
            throw new IllegalStateException("Unable to encrypt device pair key", ex);
        }
    }

    private byte[] secretKey() {
        try {
            return MessageDigest.getInstance("SHA-256")
                    .digest(pairKeySecret.getBytes(StandardCharsets.UTF_8));
        } catch (final NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm not available", ex);
        }
    }
}
