package com.warehouse.shipment.domain.vo.conf;

import com.warehouse.commonassets.identificator.DepartmentCode;

import java.util.Objects;

public record TrackingNumberRule(
        TrackingNumberPrefixMode prefixMode,
        String key,
        String separator,
        TrackingNumberSource source,
        int randomLength,
        boolean includeDate,
        TrackingNumberDateFormat dateFormat,
        boolean uppercase
) {

    public TrackingNumberRule {
        prefixMode = Objects.requireNonNullElse(prefixMode, inferPrefixMode(key));
        key = prefixMode == TrackingNumberPrefixMode.NONE
                ? ""
                : key == null ? "MGR" : key.trim();
        separator = separator == null ? "-" : separator;
        source = Objects.requireNonNullElse(source, TrackingNumberSource.SEQUENCE);
        dateFormat = Objects.requireNonNullElse(dateFormat, TrackingNumberDateFormat.YYYYMMDD);
    }

    public TrackingNumberRule(final String key,
                              final String separator,
                              final TrackingNumberSource source,
                              final int randomLength,
                              final boolean includeDate,
                              final TrackingNumberDateFormat dateFormat,
                              final boolean uppercase) {
        this(inferPrefixMode(key), key, separator, source, randomLength, includeDate, dateFormat, uppercase);
    }

    public static TrackingNumberRule defaults() {
        return new TrackingNumberRule(
                TrackingNumberPrefixMode.CUSTOM,
                "MGR",
                "-",
                TrackingNumberSource.SEQUENCE,
                8,
                true,
                TrackingNumberDateFormat.YYYYMMDD,
                true
        );
    }

    public String prefix() {
        return prefixMode == TrackingNumberPrefixMode.NONE ? "" : key;
    }

    public TrackingNumberRule withDepartmentCode(final DepartmentCode departmentCode) {
        if (prefixMode != TrackingNumberPrefixMode.DEPARTMENT_CODE) {
            return this;
        } else {
            return new TrackingNumberRule(
                    prefixMode,
                    departmentCode.getValue(),
                    separator,
                    source,
                    randomLength,
                    includeDate,
                    dateFormat,
                    uppercase
            );
        }
    }

    private static TrackingNumberPrefixMode inferPrefixMode(final String key) {
        return key == null || key.isBlank()
                ? TrackingNumberPrefixMode.NONE
                : TrackingNumberPrefixMode.CUSTOM;
    }
}
