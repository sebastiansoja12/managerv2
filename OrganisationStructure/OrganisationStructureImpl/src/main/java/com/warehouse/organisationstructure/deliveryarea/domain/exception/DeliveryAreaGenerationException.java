package com.warehouse.organisationstructure.deliveryarea.domain.exception;

public class DeliveryAreaGenerationException extends DeliveryAreaException {

    public DeliveryAreaGenerationException(final String detail) {
        super(422, "GENERATION_FAILED", detail);
    }
}
