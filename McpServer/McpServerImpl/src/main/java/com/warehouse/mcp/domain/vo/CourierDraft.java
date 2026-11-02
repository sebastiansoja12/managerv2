package com.warehouse.mcp.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;

public record CourierDraft(SupplierCode courierCode,
                           String firstName,
                           String lastName,
                           String telephoneNumber,
                           String email,
                           DepartmentCode departmentCode) {
}
