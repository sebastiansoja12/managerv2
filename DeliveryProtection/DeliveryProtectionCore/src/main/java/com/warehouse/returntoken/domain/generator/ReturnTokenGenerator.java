package com.warehouse.returntoken.domain.generator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.identificator.ShipmentId;

@Component
public class ReturnTokenGenerator {

    public static String generateReturnToken(final ShipmentId shipmentId) {
        final String shipmentIdString = shipmentId.toString();
        final byte[] hashBytes = hashShipmentId(shipmentIdString);
        final String base64EncodedHash = Base64.getEncoder().encodeToString(hashBytes);
        final String numericPortion = extractNumericPortion(base64EncodedHash);
        final String salt = RandomSaltGenerator.generateSecureRandomSalt();
        final String combined = numericPortion + salt;
        final byte[] finalHashBytes = hashShipmentId(combined);
        final int token = Math.abs(BytesToIntConverter.bytesToInt(finalHashBytes)) % 1000000;
        return String.format("%06d", token);
    }

    private static byte[] hashShipmentId(String input) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    private static String extractNumericPortion(final String input) {
        final StringBuilder numericBuilder = new StringBuilder();
        for (final char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                numericBuilder.append(c);
            }
        }
        return !numericBuilder.isEmpty() ? numericBuilder.toString() : "123456";
    }

}
