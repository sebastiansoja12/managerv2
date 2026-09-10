package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLimits;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ShipmentLimitsEmbeddable {

    @Column(name = "max_weight")
    private double maxWeight;

    @Column(name = "min_weight")
    private double minWeight;

    @Column(name = "max_length")
    private double maxLength;

    @Column(name = "max_width")
    private double maxWidth;

    @Column(name = "max_height")
    private double maxHeight;

    @Column(name = "max_shipment_value")
    private double maxShipmentValue;

    @Column(name = "shipment_allow_oversized")
    private boolean allowOversized;

    public ShipmentLimitsEmbeddable() {
    }

    public static ShipmentLimitsEmbeddable from(final ShipmentLimits limits) {
        final ShipmentLimits source = limits != null
                ? limits
                : new ShipmentLimits();
        final ShipmentLimitsEmbeddable embeddable = new ShipmentLimitsEmbeddable();
        embeddable.maxWeight = source.getMaxWeight();
        embeddable.minWeight = source.getMinWeight();
        embeddable.maxLength = source.getMaxLength();
        embeddable.maxWidth = source.getMaxWidth();
        embeddable.maxHeight = source.getMaxHeight();
        embeddable.maxShipmentValue = source.getMaxShipmentValue();
        embeddable.allowOversized = source.isAllowOversized();
        return embeddable;
    }

    public ShipmentLimits toModel() {
        return new ShipmentLimits(
                maxWeight,
                minWeight,
                maxLength,
                maxWidth,
                maxHeight,
                maxShipmentValue,
                allowOversized
        );
    }
}
