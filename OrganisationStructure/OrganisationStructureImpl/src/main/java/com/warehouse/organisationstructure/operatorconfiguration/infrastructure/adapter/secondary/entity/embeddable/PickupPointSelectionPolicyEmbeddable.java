package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.FullPickupPointPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointPreference;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointSelectionPolicy;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class PickupPointSelectionPolicyEmbeddable {

    @Column(name = "pickup_point_max_distance_km")
    private int maxDistanceKm;

    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_point_preference")
    private PickupPointPreference preference;

    @Column(name = "pickup_point_alternative_count")
    private int alternativeCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_point_full_point_policy")
    private FullPickupPointPolicy fullPointPolicy;

    @Column(name = "pickup_point_same_department_only")
    private boolean sameDepartmentOnly;

    public PickupPointSelectionPolicyEmbeddable() {
    }

    public static PickupPointSelectionPolicyEmbeddable from(final PickupPointSelectionPolicy configuration) {
        final PickupPointSelectionPolicy source = configuration != null
                ? configuration
                : PickupPointSelectionPolicy.defaultConfiguration();
        final PickupPointSelectionPolicyEmbeddable embeddable = new PickupPointSelectionPolicyEmbeddable();
        embeddable.maxDistanceKm = source.getMaxDistanceKm();
        embeddable.preference = source.getPreference();
        embeddable.alternativeCount = source.getAlternativeCount();
        embeddable.fullPointPolicy = source.getFullPointPolicy();
        embeddable.sameDepartmentOnly = source.isSameDepartmentOnly();
        return embeddable;
    }

    public PickupPointSelectionPolicy toModel() {
        return new PickupPointSelectionPolicy(
                maxDistanceKm,
                preference,
                alternativeCount,
                fullPointPolicy,
                sameDepartmentOnly
        );
    }
}
