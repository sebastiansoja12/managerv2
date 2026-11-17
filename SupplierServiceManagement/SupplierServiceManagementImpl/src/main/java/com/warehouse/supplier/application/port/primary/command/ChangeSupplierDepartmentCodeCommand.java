package com.warehouse.supplier.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;

public record ChangeSupplierDepartmentCodeCommand(SupplierCode supplierCode, DepartmentCode departmentCode) {
}
