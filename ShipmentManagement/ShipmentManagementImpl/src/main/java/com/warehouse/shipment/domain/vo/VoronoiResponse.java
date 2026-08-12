package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;

public class VoronoiResponse {

    private final DepartmentCode departmentCode;

    public VoronoiResponse(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public DepartmentCode getDepartmentCodeResult() {
        return departmentCode;
    }
}
