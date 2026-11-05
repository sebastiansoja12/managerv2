package com.warehouse.organisationstructure.deliveryarea.domain.exception;

import com.warehouse.commonassets.identificator.DepartmentId;

public class DeliveryAreaDepartmentNotFoundException extends DeliveryAreaException {

    public DeliveryAreaDepartmentNotFoundException(final DepartmentId departmentId) {
        super(404, "DEPARTMENT_NOT_FOUND", "Department " + departmentId.getValue() + " was not found");
    }
}
