package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class TrackingNumberRule {
    private TrackingNumberPrefixMode prefixMode;
    private String key;
    private String separator;
    private TrackingNumberSource source;
    private int randomLength;
    private boolean includeDate;
    private TrackingNumberDateFormat dateFormat;
    private boolean uppercase;

    public TrackingNumberRule() {
    }

    public TrackingNumberRule(final TrackingNumberPrefixMode prefixMode,
                              final String key,
                              final String separator,
                              final TrackingNumberSource source,
                              final int randomLength,
                              final boolean includeDate,
                              final TrackingNumberDateFormat dateFormat,
                              final boolean uppercase) {
        this.prefixMode = prefixMode != null ? prefixMode : inferPrefixMode(key);
        this.key = this.prefixMode == TrackingNumberPrefixMode.NONE
                ? ""
                : key;
        this.separator = separator;
        this.source = source;
        this.randomLength = randomLength;
        this.includeDate = includeDate;
        this.dateFormat = dateFormat;
        this.uppercase = uppercase;
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

    public static TrackingNumberRule defaultRule() {
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

    private static TrackingNumberPrefixMode inferPrefixMode(final String key) {
        return key == null || key.isBlank()
                ? TrackingNumberPrefixMode.NONE
                : TrackingNumberPrefixMode.CUSTOM;
    }

    public TrackingNumberPrefixMode getPrefixMode() { return prefixMode; }
    public String getKey() { return key; }
    public String getSeparator() { return separator; }
    public TrackingNumberSource getSource() { return source; }
    public int getRandomLength() { return randomLength; }
    public boolean isIncludeDate() { return includeDate; }
    public TrackingNumberDateFormat getDateFormat() { return dateFormat; }
    public boolean isUppercase() { return uppercase; }
}
