package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShipmentDeliveryConfiguration {
    private boolean requireDeliveryToken;
    private ProofOfDeliveryMode proofOfDelivery;
    private int maxDeliveryAttempts;
    private boolean allowNeighbourDelivery;
    private boolean allowSafePlaceDelivery;
    private boolean redirectToPickupPointAfterFailure;

    public ShipmentDeliveryConfiguration() {
    }

    public ShipmentDeliveryConfiguration(final boolean requireDeliveryToken,
                                         final ProofOfDeliveryMode proofOfDelivery,
                                         final int maxDeliveryAttempts,
                                         final boolean allowNeighbourDelivery,
                                         final boolean allowSafePlaceDelivery,
                                         final boolean redirectToPickupPointAfterFailure) {
        this.requireDeliveryToken = requireDeliveryToken;
        this.proofOfDelivery = proofOfDelivery;
        this.maxDeliveryAttempts = maxDeliveryAttempts;
        this.allowNeighbourDelivery = allowNeighbourDelivery;
        this.allowSafePlaceDelivery = allowSafePlaceDelivery;
        this.redirectToPickupPointAfterFailure = redirectToPickupPointAfterFailure;
    }

    public static ShipmentDeliveryConfiguration defaultConfiguration() {
        return new ShipmentDeliveryConfiguration(false, ProofOfDeliveryMode.SIGNATURE, 2, false, false, true);
    }

    public boolean isRequireDeliveryToken() { return requireDeliveryToken; }
    public ProofOfDeliveryMode getProofOfDelivery() { return proofOfDelivery; }
    public int getMaxDeliveryAttempts() { return maxDeliveryAttempts; }
    public boolean isAllowNeighbourDelivery() { return allowNeighbourDelivery; }
    public boolean isAllowSafePlaceDelivery() { return allowSafePlaceDelivery; }
    public boolean isRedirectToPickupPointAfterFailure() { return redirectToPickupPointAfterFailure; }
}
