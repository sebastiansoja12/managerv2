package com.warehouse.auth.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

public record UserDepartmentUpdateRequest(DepartmentCode departmentCode, UserId userId, String telephoneNumber, String email) {
}
