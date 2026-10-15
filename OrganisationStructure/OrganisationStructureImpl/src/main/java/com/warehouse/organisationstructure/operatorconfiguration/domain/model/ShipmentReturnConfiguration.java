package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShipmentReturnConfiguration {
    private boolean allowReturns;
    private int returnWindowDays;
    private boolean autoReturnAfterFailedDelivery;
    private int pickupPointStorageDays;
    private boolean requireReturnReason;

    public ShipmentReturnConfiguration() {
    }

    public ShipmentReturnConfiguration(final boolean allowReturns,
                                       final int returnWindowDays,
                                       final boolean autoReturnAfterFailedDelivery,
                                       final int pickupPointStorageDays,
                                       final boolean requireReturnReason) {
        this.allowReturns = allowReturns;
        this.returnWindowDays = returnWindowDays;
        this.autoReturnAfterFailedDelivery = autoReturnAfterFailedDelivery;
        this.pickupPointStorageDays = pickupPointStorageDays;
        this.requireReturnReason = requireReturnReason;
    }

    public static ShipmentReturnConfiguration defaultConfiguration() {
        return new ShipmentReturnConfiguration(true, 14, true, 7, false);
    }

    public boolean isAllowReturns() { return allowReturns; }
    public int getReturnWindowDays() { return returnWindowDays; }
    public boolean isAutoReturnAfterFailedDelivery() { return autoReturnAfterFailedDelivery; }
    public int getPickupPointStorageDays() { return pickupPointStorageDays; }
    public boolean isRequireReturnReason() { return requireReturnReason; }
}
