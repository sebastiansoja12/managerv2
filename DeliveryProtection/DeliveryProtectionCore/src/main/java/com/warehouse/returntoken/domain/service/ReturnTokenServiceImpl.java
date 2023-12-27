package com.warehouse.returntoken.domain.service;


import com.warehouse.returntoken.domain.model.Parcel;
import com.warehouse.returntoken.domain.vo.Token;
import lombok.AllArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@AllArgsConstructor
public class ReturnTokenServiceImpl implements ReturnTokenService {

    private static final int TOKEN_LENGTH = 6;

    private static String hashMD5(String input) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final byte[] messageDigest = md.digest(input.getBytes());

            final StringBuilder hexString = new StringBuilder();

            for (byte b : messageDigest) {
                hexString.append(String.format("%02X", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    @Override
    public Token determineToken(Parcel parcel) {
        final String input = parcel.getId() + "_" + parcel.getParcelRelatedId() + "_" +
                parcel.getParcelStatus();

        final String hashedInput = hashMD5(input);

        final String token = hashedInput.substring(0, TOKEN_LENGTH);

        if (!parcel.isLocked()) {
            return new Token(parcel.getId(), token);
        }
        return new Token(parcel.getId(), null);
    }
}
