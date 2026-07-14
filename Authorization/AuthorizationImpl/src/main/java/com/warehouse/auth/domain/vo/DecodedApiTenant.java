package com.warehouse.auth.domain.vo;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public record DecodedApiTenant(UserId userId, OperatorId operatorId, String username) {
}
