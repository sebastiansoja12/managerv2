package com.warehouse.organisationstructure.deliveryarea.domain.exception;

public class InvalidDeliveryAreaNameException extends DeliveryAreaException {

    public InvalidDeliveryAreaNameException(final int maxLength) {
        super(400, "INVALID_NAME", "Delivery area name must have 1-" + maxLength + " characters");
    }
}
