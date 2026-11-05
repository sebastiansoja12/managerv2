package com.warehouse.mcp.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;

public record Courier(SupplierId supplierId,
                      SupplierCode courierCode,
                      String firstName,
                      String lastName,
                      String telephoneNumber,
                      DepartmentCode departmentCode,
                      String status) {
}
