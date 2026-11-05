package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class PickupPointCapacityPolicy {

    private final int defaultCapacity;
    private final int capacityWarningThresholdPercent;
    private final boolean blockIntakeWhenFull;

    public PickupPointCapacityPolicy(final int defaultCapacity,
                                     final int capacityWarningThresholdPercent,
                                     final boolean blockIntakeWhenFull) {
        this.defaultCapacity = defaultCapacity;
        this.capacityWarningThresholdPercent = capacityWarningThresholdPercent;
        this.blockIntakeWhenFull = blockIntakeWhenFull;
    }

    public static PickupPointCapacityPolicy defaultConfiguration() {
        return new PickupPointCapacityPolicy(
                200,
                85,
                true
        );
    }

    public int getDefaultCapacity() { return defaultCapacity; }
    public int getCapacityWarningThresholdPercent() { return capacityWarningThresholdPercent; }
    public boolean isBlockIntakeWhenFull() { return blockIntakeWhenFull; }
}
