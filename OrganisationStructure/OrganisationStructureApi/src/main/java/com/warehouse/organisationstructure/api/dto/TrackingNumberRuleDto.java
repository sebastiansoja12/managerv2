package com.warehouse.organisationstructure.api.dto;

public record TrackingNumberRuleDto(
        TrackingNumberPrefixModeDto prefixMode,
        String key,
        String separator,
        TrackingNumberSourceDto source,
        int randomLength,
        boolean includeDate,
        TrackingNumberDateFormatDto dateFormat,
        boolean uppercase
) {

    public TrackingNumberRuleDto(final String key,
                                 final String separator,
                                 final TrackingNumberSourceDto source,
                                 final int randomLength,
                                 final boolean includeDate,
                                 final TrackingNumberDateFormatDto dateFormat,
                                 final boolean uppercase) {
        this(inferPrefixMode(key), key, separator, source, randomLength, includeDate, dateFormat, uppercase);
    }

    private static TrackingNumberPrefixModeDto inferPrefixMode(final String key) {
        return key == null || key.isBlank()
                ? TrackingNumberPrefixModeDto.NONE
                : TrackingNumberPrefixModeDto.CUSTOM;
    }
}
