package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ProofOfDeliveryMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentDeliveryConfiguration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class ShipmentDeliveryConfigurationEmbeddable {

    @Column(name = "shipment_require_delivery_token")
    private boolean requireDeliveryToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_proof_of_delivery")
    private ProofOfDeliveryMode proofOfDelivery;

    @Column(name = "shipment_max_delivery_attempts")
    private int maxDeliveryAttempts;

    @Column(name = "shipment_allow_neighbour_delivery")
    private boolean allowNeighbourDelivery;

    @Column(name = "shipment_allow_safe_place_delivery")
    private boolean allowSafePlaceDelivery;

    @Column(name = "shipment_redirect_to_pickup_point_after_failure")
    private boolean redirectToPickupPointAfterFailure;

    public ShipmentDeliveryConfigurationEmbeddable() {
    }

    public static ShipmentDeliveryConfigurationEmbeddable from(final ShipmentDeliveryConfiguration configuration) {
        final ShipmentDeliveryConfiguration source = configuration != null
                ? configuration
                : ShipmentDeliveryConfiguration.defaultConfiguration();
        final ShipmentDeliveryConfigurationEmbeddable embeddable = new ShipmentDeliveryConfigurationEmbeddable();
        embeddable.requireDeliveryToken = source.isRequireDeliveryToken();
        embeddable.proofOfDelivery = source.getProofOfDelivery();
        embeddable.maxDeliveryAttempts = source.getMaxDeliveryAttempts();
        embeddable.allowNeighbourDelivery = source.isAllowNeighbourDelivery();
        embeddable.allowSafePlaceDelivery = source.isAllowSafePlaceDelivery();
        embeddable.redirectToPickupPointAfterFailure = source.isRedirectToPickupPointAfterFailure();
        return embeddable;
    }

    public ShipmentDeliveryConfiguration toModel() {
        return new ShipmentDeliveryConfiguration(
                requireDeliveryToken,
                proofOfDelivery,
                maxDeliveryAttempts,
                allowNeighbourDelivery,
                allowSafePlaceDelivery,
                redirectToPickupPointAfterFailure
        );
    }
}
