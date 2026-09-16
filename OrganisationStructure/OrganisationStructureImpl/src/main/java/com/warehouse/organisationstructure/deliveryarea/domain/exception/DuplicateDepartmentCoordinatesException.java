package com.warehouse.organisationstructure.deliveryarea.domain.exception;

import com.warehouse.commonassets.identificator.DepartmentCode;

public class DuplicateDepartmentCoordinatesException extends DeliveryAreaException {

    public DuplicateDepartmentCoordinatesException(final DepartmentCode departmentCode) {
        super(400, "DUPLICATE_COORDINATES", "Department " + departmentCode.getValue()
                + " has the same coordinates as another department");
    }
}
