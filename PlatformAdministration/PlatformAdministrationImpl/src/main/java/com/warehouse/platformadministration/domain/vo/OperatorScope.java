package com.warehouse.platformadministration.domain.vo;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public record OperatorScope(OperatorId operatorId, UserId actingUserId) {
}
