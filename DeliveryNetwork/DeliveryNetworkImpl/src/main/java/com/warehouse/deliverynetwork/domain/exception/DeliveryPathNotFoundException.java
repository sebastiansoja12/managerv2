package com.warehouse.deliverynetwork.domain.exception;

import com.warehouse.commonassets.identificator.DepartmentId;

public class DeliveryPathNotFoundException extends RuntimeException {

    public DeliveryPathNotFoundException(final DepartmentId sourceDepartmentId,
                                         final DepartmentId targetDepartmentId) {
        super("Delivery path does not exist between departments: "
                + sourceDepartmentId.getValue()
                + " -> "
                + targetDepartmentId.getValue());
    }
}
