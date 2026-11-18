package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointConfiguration;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class PickupPointConfigurationEmbeddable {

    @Embedded
    private PickupPointDefaultsPolicyEmbeddable defaultsPolicy;

    @Embedded
    private PickupPointCollectionPolicyEmbeddable collectionPolicy;

    @Embedded
    private PickupPointLockerPolicyEmbeddable lockerPolicy;

    @Embedded
    private PickupPointSelectionPolicyEmbeddable selectionPolicy;

    @Embedded
    private PickupPointCapacityPolicyEmbeddable capacityPolicy;

    @Embedded
    private PickupPointLifecyclePolicyEmbeddable lifecyclePolicy;

    public PickupPointConfigurationEmbeddable() {
    }

    public static PickupPointConfigurationEmbeddable from(final PickupPointConfiguration configuration) {
        final PickupPointConfiguration source = configuration != null
                ? configuration
                : PickupPointConfiguration.defaultConfiguration();
        final PickupPointConfigurationEmbeddable embeddable = new PickupPointConfigurationEmbeddable();
        embeddable.defaultsPolicy = PickupPointDefaultsPolicyEmbeddable.from(source.getDefaultsPolicy());
        embeddable.collectionPolicy = PickupPointCollectionPolicyEmbeddable.from(source.getCollectionPolicy());
        embeddable.lockerPolicy = PickupPointLockerPolicyEmbeddable.from(source.getLockerPolicy());
        embeddable.selectionPolicy = PickupPointSelectionPolicyEmbeddable.from(source.getSelectionPolicy());
        embeddable.capacityPolicy = PickupPointCapacityPolicyEmbeddable.from(source.getCapacityPolicy());
        embeddable.lifecyclePolicy = PickupPointLifecyclePolicyEmbeddable.from(source.getLifecyclePolicy());
        return embeddable;
    }

    public PickupPointConfiguration toModel() {
        return new PickupPointConfiguration(
                defaultsPolicy != null ? defaultsPolicy.toModel() : null,
                collectionPolicy != null ? collectionPolicy.toModel() : null,
                lockerPolicy != null ? lockerPolicy.toModel() : null,
                selectionPolicy != null ? selectionPolicy.toModel() : null,
                capacityPolicy != null ? capacityPolicy.toModel() : null,
                lifecyclePolicy != null ? lifecyclePolicy.toModel() : null
        );
    }
}
