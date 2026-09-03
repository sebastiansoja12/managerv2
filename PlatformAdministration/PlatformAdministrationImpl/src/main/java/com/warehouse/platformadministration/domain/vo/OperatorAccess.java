package com.warehouse.platformadministration.domain.vo;

import com.warehouse.commonassets.identificator.OperatorId;

public record OperatorAccess(OperatorId operatorId, long activeSessions) {
}
