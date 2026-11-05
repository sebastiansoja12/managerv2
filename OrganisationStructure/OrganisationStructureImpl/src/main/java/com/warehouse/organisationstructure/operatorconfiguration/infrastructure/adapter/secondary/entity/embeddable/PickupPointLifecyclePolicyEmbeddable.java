package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointLifecyclePolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.SuspendedPickupPointShipmentPolicy;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class PickupPointLifecyclePolicyEmbeddable {

    @Column(name = "pickup_point_require_status_change_reason")
    private boolean requireStatusChangeReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_point_suspended_point_shipment_policy")
    private SuspendedPickupPointShipmentPolicy suspendedPointShipmentPolicy;

    @Column(name = "pickup_point_block_closing_with_pending_shipments")
    private boolean blockClosingWithPendingShipments;

    @Column(name = "pickup_point_auto_resume_suspended_after_days")
    private int autoResumeSuspendedAfterDays;

    public PickupPointLifecyclePolicyEmbeddable() {
    }

    public static PickupPointLifecyclePolicyEmbeddable from(final PickupPointLifecyclePolicy configuration) {
        final PickupPointLifecyclePolicy source = configuration != null
                ? configuration
                : PickupPointLifecyclePolicy.defaultConfiguration();
        final PickupPointLifecyclePolicyEmbeddable embeddable = new PickupPointLifecyclePolicyEmbeddable();
        embeddable.requireStatusChangeReason = source.isRequireStatusChangeReason();
        embeddable.suspendedPointShipmentPolicy = source.getSuspendedPointShipmentPolicy();
        embeddable.blockClosingWithPendingShipments = source.isBlockClosingWithPendingShipments();
        embeddable.autoResumeSuspendedAfterDays = source.getAutoResumeSuspendedAfterDays();
        return embeddable;
    }

    public PickupPointLifecyclePolicy toModel() {
        return new PickupPointLifecyclePolicy(
                requireStatusChangeReason,
                suspendedPointShipmentPolicy,
                blockClosingWithPendingShipments,
                autoResumeSuspendedAfterDays
        );
    }
}
