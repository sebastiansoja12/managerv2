package com.warehouse.mcp.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;

public record DeliveryFilter(DepartmentCode departmentCode, SupplierCode courierCode, Integer limit) {
}
