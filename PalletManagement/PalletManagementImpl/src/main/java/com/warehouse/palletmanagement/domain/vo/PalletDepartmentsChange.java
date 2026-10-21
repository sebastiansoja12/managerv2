package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;

public record PalletDepartmentsChange(DepartmentId originDepartmentId, DepartmentId destinationDepartmentId) {
}
