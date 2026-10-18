package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;

public record PalletDepartment(DepartmentId departmentId,
                               String departmentCode,
                               String city,
                               String street,
                               String zipCode,
                               String countryCode) {

    public static PalletDepartment unknown(final DepartmentId departmentId) {
        return new PalletDepartment(departmentId, departmentId == null ? null : String.valueOf(departmentId.getValue()),
                null, null, null, null);
    }
}
