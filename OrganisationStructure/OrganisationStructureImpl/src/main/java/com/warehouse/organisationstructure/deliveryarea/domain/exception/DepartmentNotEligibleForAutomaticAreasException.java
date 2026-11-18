package com.warehouse.organisationstructure.deliveryarea.domain.exception;

import com.warehouse.commonassets.identificator.DepartmentCode;

public class DepartmentNotEligibleForAutomaticAreasException extends DeliveryAreaException {

    public DepartmentNotEligibleForAutomaticAreasException(final DepartmentCode departmentCode) {
        super(400, "DEPARTMENT_NOT_ELIGIBLE", "Department " + departmentCode.getValue()
                + " must be active, located in Poland and have valid coordinates");
    }
}
