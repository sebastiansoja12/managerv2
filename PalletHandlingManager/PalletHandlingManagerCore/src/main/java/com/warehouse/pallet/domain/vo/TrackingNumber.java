package com.warehouse.pallet.domain.vo;

import org.apache.commons.lang3.StringUtils;

public record TrackingNumber(String value) {

    public TrackingNumber {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Tracking number cannot be blank");
        }
        value = value.trim().toUpperCase();
    }
}
