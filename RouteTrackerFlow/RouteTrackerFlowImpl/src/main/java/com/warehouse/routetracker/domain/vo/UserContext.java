package com.warehouse.routetracker.domain.vo;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public record UserContext(UserId userId, OperatorId operatorId) {
}
