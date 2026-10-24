package com.warehouse.supplierconfiguration.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;

public record SupplierProfile(SupplierId supplierId,
                              SupplierCode supplierCode,
                              String firstName,
                              String lastName,
                              DepartmentId departmentId,
                              boolean active) {
}
