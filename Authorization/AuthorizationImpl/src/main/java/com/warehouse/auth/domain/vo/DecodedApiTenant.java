package com.warehouse.auth.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

public record DecodedApiTenant(UserId userId, DepartmentCode departmentCode, String username) {
}
