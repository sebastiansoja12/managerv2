package com.warehouse.chat.domain.vo;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public record ChatPresence(String sessionId, UserId userId, OperatorId operatorId) {
}
