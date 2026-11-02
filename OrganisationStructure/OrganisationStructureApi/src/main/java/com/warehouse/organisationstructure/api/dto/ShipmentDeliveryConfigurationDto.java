package com.warehouse.organisationstructure.api.dto;

public record ShipmentDeliveryConfigurationDto(
        boolean requireDeliveryToken,
        ProofOfDeliveryModeDto proofOfDelivery,
        int maxDeliveryAttempts,
        boolean allowNeighbourDelivery,
        boolean allowSafePlaceDelivery,
        boolean redirectToPickupPointAfterFailure
) {
}
