package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberDateFormat;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberRule;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberSource;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class TrackingNumberRuleEmbeddable {

    @Column(name = "shipment_tracking_number_key")
    private String key;

    @Column(name = "shipment_tracking_number_separator")
    private String separator;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_tracking_number_source")
    private TrackingNumberSource source;

    @Column(name = "shipment_tracking_number_random_length")
    private int randomLength;

    @Column(name = "shipment_tracking_number_include_date")
    private boolean includeDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_tracking_number_date_format")
    private TrackingNumberDateFormat dateFormat;

    @Column(name = "shipment_tracking_number_uppercase")
    private boolean uppercase;

    public TrackingNumberRuleEmbeddable() {
    }

    public static TrackingNumberRuleEmbeddable from(final TrackingNumberRule rule) {
        final TrackingNumberRule source = rule != null
                ? rule
                : TrackingNumberRule.defaultRule();
        final TrackingNumberRuleEmbeddable embeddable = new TrackingNumberRuleEmbeddable();
        embeddable.key = source.getKey();
        embeddable.separator = source.getSeparator();
        embeddable.source = source.getSource();
        embeddable.randomLength = source.getRandomLength();
        embeddable.includeDate = source.isIncludeDate();
        embeddable.dateFormat = source.getDateFormat();
        embeddable.uppercase = source.isUppercase();
        return embeddable;
    }

    public TrackingNumberRule toModel() {
        return new TrackingNumberRule(
                key,
                separator,
                source,
                randomLength,
                includeDate,
                dateFormat,
                uppercase
        );
    }
}
