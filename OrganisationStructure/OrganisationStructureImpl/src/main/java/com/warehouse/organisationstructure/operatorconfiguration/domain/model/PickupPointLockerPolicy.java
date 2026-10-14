package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class PickupPointLockerPolicy {

    private final int courierCompartmentHoldMinutes;
    private final int compartmentReservationHours;
    private final OversizedLockerShipmentPolicy oversizedShipmentPolicy;
    private final int suspendAfterCompartmentFailures;

    public PickupPointLockerPolicy(final int courierCompartmentHoldMinutes,
                                   final int compartmentReservationHours,
                                   final OversizedLockerShipmentPolicy oversizedShipmentPolicy,
                                   final int suspendAfterCompartmentFailures) {
        this.courierCompartmentHoldMinutes = courierCompartmentHoldMinutes;
        this.compartmentReservationHours = compartmentReservationHours;
        this.oversizedShipmentPolicy = oversizedShipmentPolicy;
        this.suspendAfterCompartmentFailures = suspendAfterCompartmentFailures;
    }

    public static PickupPointLockerPolicy defaultConfiguration() {
        return new PickupPointLockerPolicy(
                15,
                24,
                OversizedLockerShipmentPolicy.REDIRECT_TO_SERVICE_POINT,
                3
        );
    }

    public int getCourierCompartmentHoldMinutes() { return courierCompartmentHoldMinutes; }
    public int getCompartmentReservationHours() { return compartmentReservationHours; }
    public OversizedLockerShipmentPolicy getOversizedShipmentPolicy() { return oversizedShipmentPolicy; }
    public int getSuspendAfterCompartmentFailures() { return suspendAfterCompartmentFailures; }
}
