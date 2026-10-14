package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class PickupPointSelectionPolicy {

    private final int maxDistanceKm;
    private final PickupPointPreference preference;
    private final int alternativeCount;
    private final FullPickupPointPolicy fullPointPolicy;
    private final boolean sameDepartmentOnly;

    public PickupPointSelectionPolicy(final int maxDistanceKm,
                                      final PickupPointPreference preference,
                                      final int alternativeCount,
                                      final FullPickupPointPolicy fullPointPolicy,
                                      final boolean sameDepartmentOnly) {
        this.maxDistanceKm = maxDistanceKm;
        this.preference = preference;
        this.alternativeCount = alternativeCount;
        this.fullPointPolicy = fullPointPolicy;
        this.sameDepartmentOnly = sameDepartmentOnly;
    }

    public static PickupPointSelectionPolicy defaultConfiguration() {
        return new PickupPointSelectionPolicy(
                5,
                PickupPointPreference.NEAREST,
                3,
                FullPickupPointPolicy.SELECT_NEXT,
                false
        );
    }

    public int getMaxDistanceKm() { return maxDistanceKm; }
    public PickupPointPreference getPreference() { return preference; }
    public int getAlternativeCount() { return alternativeCount; }
    public FullPickupPointPolicy getFullPointPolicy() { return fullPointPolicy; }
    public boolean isSameDepartmentOnly() { return sameDepartmentOnly; }
}
