package com.warehouse.mcp.domain.vo;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public record CurrentUserProfile(UserId userId,
                                 String username,
                                 String firstName,
                                 String lastName,
                                 String role,
                                 String departmentCode,
                                 OperatorId operatorId) {
}
