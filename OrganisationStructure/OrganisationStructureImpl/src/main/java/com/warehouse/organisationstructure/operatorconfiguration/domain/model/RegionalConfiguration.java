package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class RegionalConfiguration {

    private final String timeZone;
    private final RegionalDateFormat dateFormat;
    private final CurrencyCode currency;
    private final WeightUnit weightUnit;
    private final LengthUnit lengthUnit;
    private final String workingDayStart;
    private final String workingDayEnd;
    private final boolean saturdayWorkingDay;
    private final boolean sundayWorkingDay;

    public RegionalConfiguration(final String timeZone,
                                 final RegionalDateFormat dateFormat,
                                 final CurrencyCode currency,
                                 final WeightUnit weightUnit,
                                 final LengthUnit lengthUnit,
                                 final String workingDayStart,
                                 final String workingDayEnd,
                                 final boolean saturdayWorkingDay,
                                 final boolean sundayWorkingDay) {
        this.timeZone = timeZone;
        this.dateFormat = dateFormat;
        this.currency = currency;
        this.weightUnit = weightUnit;
        this.lengthUnit = lengthUnit;
        this.workingDayStart = workingDayStart;
        this.workingDayEnd = workingDayEnd;
        this.saturdayWorkingDay = saturdayWorkingDay;
        this.sundayWorkingDay = sundayWorkingDay;
    }

    public static RegionalConfiguration defaultConfiguration() {
        return new RegionalConfiguration(
                "Europe/Warsaw",
                RegionalDateFormat.DD_MM_YYYY,
                CurrencyCode.PLN,
                WeightUnit.KG,
                LengthUnit.CM,
                "08:00",
                "16:00",
                false,
                false
        );
    }

    public String getTimeZone() { return timeZone; }
    public RegionalDateFormat getDateFormat() { return dateFormat; }
    public CurrencyCode getCurrency() { return currency; }
    public WeightUnit getWeightUnit() { return weightUnit; }
    public LengthUnit getLengthUnit() { return lengthUnit; }
    public String getWorkingDayStart() { return workingDayStart; }
    public String getWorkingDayEnd() { return workingDayEnd; }
    public boolean isSaturdayWorkingDay() { return saturdayWorkingDay; }
    public boolean isSundayWorkingDay() { return sundayWorkingDay; }
}
