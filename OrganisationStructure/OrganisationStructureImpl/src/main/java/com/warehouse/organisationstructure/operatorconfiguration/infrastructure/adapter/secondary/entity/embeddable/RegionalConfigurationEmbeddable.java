package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CurrencyCode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.LengthUnit;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RegionalConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RegionalDateFormat;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.WeightUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class RegionalConfigurationEmbeddable {

    @Column(name = "regional_time_zone")
    private String timeZone;

    @Enumerated(EnumType.STRING)
    @Column(name = "regional_date_format")
    private RegionalDateFormat dateFormat;

    @Enumerated(EnumType.STRING)
    @Column(name = "regional_currency")
    private CurrencyCode currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "regional_weight_unit")
    private WeightUnit weightUnit;

    @Enumerated(EnumType.STRING)
    @Column(name = "regional_length_unit")
    private LengthUnit lengthUnit;

    @Column(name = "regional_working_day_start")
    private String workingDayStart;

    @Column(name = "regional_working_day_end")
    private String workingDayEnd;

    @Column(name = "regional_saturday_working_day")
    private boolean saturdayWorkingDay;

    @Column(name = "regional_sunday_working_day")
    private boolean sundayWorkingDay;

    public RegionalConfigurationEmbeddable() {
    }

    public static RegionalConfigurationEmbeddable from(final RegionalConfiguration configuration) {
        final RegionalConfiguration source = configuration != null
                ? configuration
                : RegionalConfiguration.defaultConfiguration();
        final RegionalConfigurationEmbeddable embeddable = new RegionalConfigurationEmbeddable();
        embeddable.timeZone = source.getTimeZone();
        embeddable.dateFormat = source.getDateFormat();
        embeddable.currency = source.getCurrency();
        embeddable.weightUnit = source.getWeightUnit();
        embeddable.lengthUnit = source.getLengthUnit();
        embeddable.workingDayStart = source.getWorkingDayStart();
        embeddable.workingDayEnd = source.getWorkingDayEnd();
        embeddable.saturdayWorkingDay = source.isSaturdayWorkingDay();
        embeddable.sundayWorkingDay = source.isSundayWorkingDay();
        return embeddable;
    }

    public RegionalConfiguration toModel() {
        return new RegionalConfiguration(
                timeZone,
                dateFormat,
                currency,
                weightUnit,
                lengthUnit,
                workingDayStart,
                workingDayEnd,
                saturdayWorkingDay,
                sundayWorkingDay
        );
    }
}
