package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShipmentWorkflowConfiguration {
    private DefaultShipmentStatus defaultStatus;
    private ServiceLevel defaultServiceLevel;
    private boolean autoAssignCourier;
    private boolean autoCloseDelivered;
    private boolean generateTrackingNumber;
    private int cancellationWindowMinutes;
    private String pickupCutoffTime;

    public ShipmentWorkflowConfiguration() {
    }

    public ShipmentWorkflowConfiguration(final DefaultShipmentStatus defaultStatus,
                                         final ServiceLevel defaultServiceLevel,
                                         final boolean autoAssignCourier,
                                         final boolean autoCloseDelivered,
                                         final boolean generateTrackingNumber,
                                         final int cancellationWindowMinutes,
                                         final String pickupCutoffTime) {
        this.defaultStatus = defaultStatus;
        this.defaultServiceLevel = defaultServiceLevel;
        this.autoAssignCourier = autoAssignCourier;
        this.autoCloseDelivered = autoCloseDelivered;
        this.generateTrackingNumber = generateTrackingNumber;
        this.cancellationWindowMinutes = cancellationWindowMinutes;
        this.pickupCutoffTime = pickupCutoffTime;
    }

    public static ShipmentWorkflowConfiguration defaultConfiguration() {
        return new ShipmentWorkflowConfiguration(
                DefaultShipmentStatus.CREATED,
                ServiceLevel.STANDARD,
                false,
                true,
                false,
                30,
                "16:00"
        );
    }

    public DefaultShipmentStatus getDefaultStatus() { return defaultStatus; }
    public ServiceLevel getDefaultServiceLevel() { return defaultServiceLevel; }
    public boolean isAutoAssignCourier() { return autoAssignCourier; }
    public boolean isAutoCloseDelivered() { return autoCloseDelivered; }
    public boolean isGenerateTrackingNumber() { return generateTrackingNumber; }
    public int getCancellationWindowMinutes() { return cancellationWindowMinutes; }
    public String getPickupCutoffTime() { return pickupCutoffTime; }
}
