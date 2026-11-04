package com.warehouse.organisationstructure.deliveryarea.domain.exception;

public class ManualDeliveryAreaEditDisabledException extends DeliveryAreaException {

    public ManualDeliveryAreaEditDisabledException() {
        super(409, "MANUAL_EDIT_DISABLED", "Manual delivery area editing is disabled in department configuration");
    }
}
