package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DefaultShipmentStatus;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ServiceLevel;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentWorkflowConfiguration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class ShipmentWorkflowConfigurationEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_default_status")
    private DefaultShipmentStatus defaultStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_default_service_level")
    private ServiceLevel defaultServiceLevel;

    @Column(name = "shipment_auto_assign_courier")
    private boolean autoAssignCourier;

    @Column(name = "shipment_auto_close_delivered")
    private boolean autoCloseDelivered;

    @Column(name = "shipment_generate_tracking_number")
    private boolean generateTrackingNumber;

    @Column(name = "shipment_cancellation_window_minutes")
    private int cancellationWindowMinutes;

    @Column(name = "shipment_pickup_cutoff_time")
    private String pickupCutoffTime;

    public ShipmentWorkflowConfigurationEmbeddable() {
    }

    public static ShipmentWorkflowConfigurationEmbeddable from(
            final ShipmentWorkflowConfiguration configuration) {
        final ShipmentWorkflowConfiguration source = configuration != null
                ? configuration
                : ShipmentWorkflowConfiguration.defaultConfiguration();
        final ShipmentWorkflowConfigurationEmbeddable embeddable = new ShipmentWorkflowConfigurationEmbeddable();
        embeddable.defaultStatus = source.getDefaultStatus();
        embeddable.defaultServiceLevel = source.getDefaultServiceLevel();
        embeddable.autoAssignCourier = source.isAutoAssignCourier();
        embeddable.autoCloseDelivered = source.isAutoCloseDelivered();
        embeddable.generateTrackingNumber = source.isGenerateTrackingNumber();
        embeddable.cancellationWindowMinutes = source.getCancellationWindowMinutes();
        embeddable.pickupCutoffTime = source.getPickupCutoffTime();
        return embeddable;
    }

    public ShipmentWorkflowConfiguration toModel() {
        return new ShipmentWorkflowConfiguration(
                defaultStatus,
                defaultServiceLevel,
                autoAssignCourier,
                autoCloseDelivered,
                generateTrackingNumber,
                cancellationWindowMinutes,
                pickupCutoffTime
        );
    }
}
