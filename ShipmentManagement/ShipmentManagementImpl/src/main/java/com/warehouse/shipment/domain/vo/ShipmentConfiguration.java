package com.warehouse.shipment.domain.vo;

public class ShipmentConfiguration {

    private static final ShipmentConfiguration DEFAULT =
            new ShipmentConfiguration(false, false, false);

    private final boolean forceUpdate;
    private final boolean publishInReturnManager;
    private final boolean customRerouteDepartment;

    public ShipmentConfiguration(final boolean forceUpdate,
                                  final boolean publishInReturnManager,
                                  final boolean customRerouteDepartment) {
        this.forceUpdate = forceUpdate;
        this.publishInReturnManager = publishInReturnManager;
        this.customRerouteDepartment = customRerouteDepartment;
    }

    public static ShipmentConfiguration of(final boolean forceUpdate,
                                           final boolean publishInReturnManager,
                                           final boolean customRerouteDepartment) {
        return new ShipmentConfiguration(
                forceUpdate,
                publishInReturnManager,
                customRerouteDepartment
        );
    }

    public static ShipmentConfiguration defaults() {
        return DEFAULT;
    }

    public boolean forceUpdate() {
        return forceUpdate;
    }

    public boolean publishInReturnManager() {
        return publishInReturnManager;
    }

    public boolean customRerouteDepartment() {
        return customRerouteDepartment;
    }

    public boolean requiresStateValidation() {
        return !forceUpdate;
    }

    public boolean shouldPublishInReturnManager() {
        return publishInReturnManager;
    }
}
