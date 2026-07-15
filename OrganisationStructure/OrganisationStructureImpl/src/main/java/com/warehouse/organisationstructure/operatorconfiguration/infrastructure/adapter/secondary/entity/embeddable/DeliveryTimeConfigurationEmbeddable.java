package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DeliveryTimeConfiguration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DeliveryTimeConfigurationEmbeddable {

    @Column(name = "min_delivery_days")
    private int minDeliveryDays;

    @Column(name = "max_delivery_days")
    private int maxDeliveryDays;

    @Column(name = "express_delivery_days")
    private int expressDeliveryDays;

    @Column(name = "same_day_delivery_hours")
    private int sameDayDeliveryHours;

    @Column(name = "international_delivery_days")
    private int internationalDeliveryDays;

    public DeliveryTimeConfigurationEmbeddable() {
    }

    public static DeliveryTimeConfigurationEmbeddable from(
            final DeliveryTimeConfiguration configuration) {
        final DeliveryTimeConfiguration source = configuration != null
                ? configuration
                : new DeliveryTimeConfiguration();
        final DeliveryTimeConfigurationEmbeddable embeddable = new DeliveryTimeConfigurationEmbeddable();
        embeddable.minDeliveryDays = source.getMinDeliveryDays();
        embeddable.maxDeliveryDays = source.getMaxDeliveryDays();
        embeddable.expressDeliveryDays = source.getExpressDeliveryDays();
        embeddable.sameDayDeliveryHours = source.getSameDayDeliveryHours();
        embeddable.internationalDeliveryDays = source.getInternationalDeliveryDays();
        return embeddable;
    }

    public DeliveryTimeConfiguration toModel() {
        return new DeliveryTimeConfiguration(
                minDeliveryDays,
                maxDeliveryDays,
                expressDeliveryDays,
                sameDayDeliveryHours,
                internationalDeliveryDays
        );
    }
}
