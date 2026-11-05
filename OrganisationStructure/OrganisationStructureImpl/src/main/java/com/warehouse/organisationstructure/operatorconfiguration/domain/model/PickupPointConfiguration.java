package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class PickupPointConfiguration {

    private final PickupPointDefaultsPolicy defaultsPolicy;
    private final PickupPointCollectionPolicy collectionPolicy;
    private final PickupPointLockerPolicy lockerPolicy;
    private final PickupPointSelectionPolicy selectionPolicy;
    private final PickupPointCapacityPolicy capacityPolicy;
    private final PickupPointLifecyclePolicy lifecyclePolicy;

    public PickupPointConfiguration(final PickupPointDefaultsPolicy defaultsPolicy,
                                    final PickupPointCollectionPolicy collectionPolicy,
                                    final PickupPointLockerPolicy lockerPolicy,
                                    final PickupPointSelectionPolicy selectionPolicy,
                                    final PickupPointCapacityPolicy capacityPolicy,
                                    final PickupPointLifecyclePolicy lifecyclePolicy) {
        this.defaultsPolicy = defaultsPolicy != null ? defaultsPolicy : PickupPointDefaultsPolicy.defaultConfiguration();
        this.collectionPolicy = collectionPolicy != null ? collectionPolicy : PickupPointCollectionPolicy.defaultConfiguration();
        this.lockerPolicy = lockerPolicy != null ? lockerPolicy : PickupPointLockerPolicy.defaultConfiguration();
        this.selectionPolicy = selectionPolicy != null ? selectionPolicy : PickupPointSelectionPolicy.defaultConfiguration();
        this.capacityPolicy = capacityPolicy != null ? capacityPolicy : PickupPointCapacityPolicy.defaultConfiguration();
        this.lifecyclePolicy = lifecyclePolicy != null ? lifecyclePolicy : PickupPointLifecyclePolicy.defaultConfiguration();
    }

    public static PickupPointConfiguration defaultConfiguration() {
        return new PickupPointConfiguration(null, null, null, null, null, null);
    }

    public PickupPointDefaultsPolicy getDefaultsPolicy() { return defaultsPolicy; }
    public PickupPointCollectionPolicy getCollectionPolicy() { return collectionPolicy; }
    public PickupPointLockerPolicy getLockerPolicy() { return lockerPolicy; }
    public PickupPointSelectionPolicy getSelectionPolicy() { return selectionPolicy; }
    public PickupPointCapacityPolicy getCapacityPolicy() { return capacityPolicy; }
    public PickupPointLifecyclePolicy getLifecyclePolicy() { return lifecyclePolicy; }
}
