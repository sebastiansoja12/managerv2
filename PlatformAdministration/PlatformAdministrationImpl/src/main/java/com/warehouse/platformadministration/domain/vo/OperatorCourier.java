package com.warehouse.platformadministration.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;

public record OperatorCourier(SupplierId supplierId,
                              SupplierCode supplierCode,
                              String firstName,
                              String lastName,
                              String telephoneNumber,
                              DepartmentCode departmentCode,
                              String status) {
}
