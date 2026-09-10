package com.warehouse.deliverynetwork.domain.exception;

import com.warehouse.commonassets.identificator.DepartmentCode;

import java.util.List;

public class MissingSortingFacilityConnectionException extends RuntimeException {

    private final List<DepartmentCode> departmentCodes;

    public MissingSortingFacilityConnectionException(final List<DepartmentCode> departmentCodes) {
        super("Departments without a direct sorting facility connection: " + departmentCodes);
        this.departmentCodes = List.copyOf(departmentCodes);
    }

    public List<DepartmentCode> departmentCodes() {
        return this.departmentCodes;
    }
}
