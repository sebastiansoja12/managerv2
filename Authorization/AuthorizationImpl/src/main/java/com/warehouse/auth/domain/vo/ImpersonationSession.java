package com.warehouse.auth.domain.vo;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public record ImpersonationSession(String accessToken, UserId userId, String username, OperatorId operatorId) {
}
