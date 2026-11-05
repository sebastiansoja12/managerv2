package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class PickupPointLifecyclePolicy {

    private final boolean requireStatusChangeReason;
    private final SuspendedPickupPointShipmentPolicy suspendedPointShipmentPolicy;
    private final boolean blockClosingWithPendingShipments;
    private final int autoResumeSuspendedAfterDays;

    public PickupPointLifecyclePolicy(final boolean requireStatusChangeReason,
                                      final SuspendedPickupPointShipmentPolicy suspendedPointShipmentPolicy,
                                      final boolean blockClosingWithPendingShipments,
                                      final int autoResumeSuspendedAfterDays) {
        this.requireStatusChangeReason = requireStatusChangeReason;
        this.suspendedPointShipmentPolicy = suspendedPointShipmentPolicy;
        this.blockClosingWithPendingShipments = blockClosingWithPendingShipments;
        this.autoResumeSuspendedAfterDays = autoResumeSuspendedAfterDays;
    }

    public static PickupPointLifecyclePolicy defaultConfiguration() {
        return new PickupPointLifecyclePolicy(
                true,
                SuspendedPickupPointShipmentPolicy.REDIRECT,
                true,
                0
        );
    }

    public boolean isRequireStatusChangeReason() { return requireStatusChangeReason; }
    public SuspendedPickupPointShipmentPolicy getSuspendedPointShipmentPolicy() { return suspendedPointShipmentPolicy; }
    public boolean isBlockClosingWithPendingShipments() { return blockClosingWithPendingShipments; }
    public int getAutoResumeSuspendedAfterDays() { return autoResumeSuspendedAfterDays; }
}
