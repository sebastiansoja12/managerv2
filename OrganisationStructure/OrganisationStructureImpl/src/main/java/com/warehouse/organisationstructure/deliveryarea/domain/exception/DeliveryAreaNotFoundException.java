package com.warehouse.organisationstructure.deliveryarea.domain.exception;

import com.warehouse.commonassets.identificator.DeliveryAreaId;

public class DeliveryAreaNotFoundException extends DeliveryAreaException {

    public DeliveryAreaNotFoundException(final DeliveryAreaId deliveryAreaId) {
        super(404, "AREA_NOT_FOUND", "Delivery area " + deliveryAreaId.value() + " was not found");
    }
}
