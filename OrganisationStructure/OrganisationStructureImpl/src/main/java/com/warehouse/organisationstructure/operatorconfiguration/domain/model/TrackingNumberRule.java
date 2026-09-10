package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class TrackingNumberRule {
    private String key;
    private String separator;
    private TrackingNumberSource source;
    private int randomLength;
    private boolean includeDate;
    private TrackingNumberDateFormat dateFormat;
    private boolean uppercase;

    public TrackingNumberRule() {
    }

    public TrackingNumberRule(final String key,
                              final String separator,
                              final TrackingNumberSource source,
                              final int randomLength,
                              final boolean includeDate,
                              final TrackingNumberDateFormat dateFormat,
                              final boolean uppercase) {
        this.key = key;
        this.separator = separator;
        this.source = source;
        this.randomLength = randomLength;
        this.includeDate = includeDate;
        this.dateFormat = dateFormat;
        this.uppercase = uppercase;
    }

    public static TrackingNumberRule defaultRule() {
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

    public String getKey() { return key; }
    public String getSeparator() { return separator; }
    public TrackingNumberSource getSource() { return source; }
    public int getRandomLength() { return randomLength; }
    public boolean isIncludeDate() { return includeDate; }
    public TrackingNumberDateFormat getDateFormat() { return dateFormat; }
    public boolean isUppercase() { return uppercase; }
}
