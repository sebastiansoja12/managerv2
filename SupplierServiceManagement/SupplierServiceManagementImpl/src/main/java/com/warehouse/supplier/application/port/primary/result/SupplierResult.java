package com.warehouse.supplier.application.port.primary.result;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;

public record SupplierResult(SupplierSnapshot supplier, DepartmentCode departmentCode) {
}
