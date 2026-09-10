package com.warehouse.shipment.domain.vo.conf;

import java.util.Objects;

public record TrackingNumberRule(
        String key,
        String separator,
        TrackingNumberSource source,
        int randomLength,
        boolean includeDate,
        TrackingNumberDateFormat dateFormat,
        boolean uppercase
) {

    public TrackingNumberRule {
        key = key == null || key.isBlank() ? "MGR" : key;
        separator = separator == null ? "-" : separator;
        source = Objects.requireNonNullElse(source, TrackingNumberSource.SEQUENCE);
        dateFormat = Objects.requireNonNullElse(dateFormat, TrackingNumberDateFormat.YYYYMMDD);
    }

    public static TrackingNumberRule defaults() {
        return new TrackingNumberRule(
                "MGR",
                "-",
                TrackingNumberSource.SEQUENCE,
                8,
                true,
                TrackingNumberDateFormat.YYYYMMDD,
                true
        );
    }
}
