package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointCapacityPolicy;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PickupPointCapacityPolicyEmbeddable {

    @Column(name = "pickup_point_default_capacity")
    private int defaultCapacity;

    @Column(name = "pickup_point_capacity_warning_threshold_percent")
    private int capacityWarningThresholdPercent;

    @Column(name = "pickup_point_block_intake_when_full")
    private boolean blockIntakeWhenFull;

    public PickupPointCapacityPolicyEmbeddable() {
    }

    public static PickupPointCapacityPolicyEmbeddable from(final PickupPointCapacityPolicy configuration) {
        final PickupPointCapacityPolicy source = configuration != null
                ? configuration
                : PickupPointCapacityPolicy.defaultConfiguration();
        final PickupPointCapacityPolicyEmbeddable embeddable = new PickupPointCapacityPolicyEmbeddable();
        embeddable.defaultCapacity = source.getDefaultCapacity();
        embeddable.capacityWarningThresholdPercent = source.getCapacityWarningThresholdPercent();
        embeddable.blockIntakeWhenFull = source.isBlockIntakeWhenFull();
        return embeddable;
    }

    public PickupPointCapacityPolicy toModel() {
        return new PickupPointCapacityPolicy(
                defaultCapacity,
                capacityWarningThresholdPercent,
                blockIntakeWhenFull
        );
    }
}
