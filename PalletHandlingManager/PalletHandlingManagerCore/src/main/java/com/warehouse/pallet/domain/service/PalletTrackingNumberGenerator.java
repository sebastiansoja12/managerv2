package com.warehouse.pallet.domain.service;

import com.warehouse.pallet.domain.vo.TrackingNumber;

import java.security.SecureRandom;

public class PalletTrackingNumberGenerator {

    private static final String PREFIX = "PAL";
    private static final int DIGITS = 12;

    private final SecureRandom random = new SecureRandom();

    public TrackingNumber generate() {
        final StringBuilder value = new StringBuilder(PREFIX);
        for (int index = 0; index < DIGITS; index++) {
            value.append(this.random.nextInt(10));
        }
        return new TrackingNumber(value.toString());
    }
}
