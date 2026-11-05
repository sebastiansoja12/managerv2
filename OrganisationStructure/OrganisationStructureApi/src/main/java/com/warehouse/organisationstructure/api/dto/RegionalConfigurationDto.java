package com.warehouse.organisationstructure.api.dto;

public record RegionalConfigurationDto(
        String timeZone,
        RegionalDateFormatDto dateFormat,
        CurrencyCodeDto currency,
        WeightUnitDto weightUnit,
        LengthUnitDto lengthUnit,
        String workingDayStart,
        String workingDayEnd,
        boolean saturdayWorkingDay,
        boolean sundayWorkingDay
) {
}
