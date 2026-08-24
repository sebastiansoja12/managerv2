package com.warehouse.organisationstructure.api.dto;

public record TrackingNumberRuleDto(
        String key,
        String separator,
        TrackingNumberSourceDto source,
        int randomLength,
        boolean includeDate,
        TrackingNumberDateFormatDto dateFormat,
        boolean uppercase
) {
}
