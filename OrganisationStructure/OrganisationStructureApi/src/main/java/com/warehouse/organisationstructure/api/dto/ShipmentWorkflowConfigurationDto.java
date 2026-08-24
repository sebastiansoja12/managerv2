package com.warehouse.organisationstructure.api.dto;

public record ShipmentWorkflowConfigurationDto(
        DefaultShipmentStatusDto defaultStatus,
        ShipmentServiceLevelDto defaultServiceLevel,
        boolean autoAssignCourier,
        boolean autoCloseDelivered,
        boolean generateTrackingNumber,
        int cancellationWindowMinutes,
        String pickupCutoffTime
) {
}
