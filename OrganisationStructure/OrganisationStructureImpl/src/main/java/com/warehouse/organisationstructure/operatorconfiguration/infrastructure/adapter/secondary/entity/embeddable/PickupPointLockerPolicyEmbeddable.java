package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OversizedLockerShipmentPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointLockerPolicy;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class PickupPointLockerPolicyEmbeddable {

    @Column(name = "pickup_point_courier_compartment_hold_minutes")
    private int courierCompartmentHoldMinutes;

    @Column(name = "pickup_point_compartment_reservation_hours")
    private int compartmentReservationHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_point_oversized_shipment_policy")
    private OversizedLockerShipmentPolicy oversizedShipmentPolicy;

    @Column(name = "pickup_point_suspend_after_compartment_failures")
    private int suspendAfterCompartmentFailures;

    public PickupPointLockerPolicyEmbeddable() {
    }

    public static PickupPointLockerPolicyEmbeddable from(final PickupPointLockerPolicy configuration) {
        final PickupPointLockerPolicy source = configuration != null
                ? configuration
                : PickupPointLockerPolicy.defaultConfiguration();
        final PickupPointLockerPolicyEmbeddable embeddable = new PickupPointLockerPolicyEmbeddable();
        embeddable.courierCompartmentHoldMinutes = source.getCourierCompartmentHoldMinutes();
        embeddable.compartmentReservationHours = source.getCompartmentReservationHours();
        embeddable.oversizedShipmentPolicy = source.getOversizedShipmentPolicy();
        embeddable.suspendAfterCompartmentFailures = source.getSuspendAfterCompartmentFailures();
        return embeddable;
    }

    public PickupPointLockerPolicy toModel() {
        return new PickupPointLockerPolicy(
                courierCompartmentHoldMinutes,
                compartmentReservationHours,
                oversizedShipmentPolicy,
                suspendAfterCompartmentFailures
        );
    }
}
