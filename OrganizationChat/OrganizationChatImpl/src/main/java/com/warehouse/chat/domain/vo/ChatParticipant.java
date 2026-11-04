package com.warehouse.chat.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public record ChatParticipant(UserId userId,
                              String username,
                              String firstName,
                              String lastName,
                              DepartmentCode departmentCode,
                              String role,
                              OperatorId operatorId) {
}
