package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

public record UserContext(UserId userId, DepartmentCode departmentCode) {
}
