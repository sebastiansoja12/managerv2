package com.warehouse.pickuppoint.application.exception;

import com.warehouse.commonassets.identificator.DepartmentId;

public class PickupPointDepartmentNotFoundException extends RuntimeException {

    public PickupPointDepartmentNotFoundException(final DepartmentId departmentId) {
        super("Pickup point department not found: " + departmentId.value());
    }
}
