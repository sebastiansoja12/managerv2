package com.warehouse.chat.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public record ChatParticipantDto(UserId userId,
                                 String username,
                                 String firstName,
                                 String lastName,
                                 String departmentCode,
                                 String role,
                                 OperatorId operatorId) {
}
