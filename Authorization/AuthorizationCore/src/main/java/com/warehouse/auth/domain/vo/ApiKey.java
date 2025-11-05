package com.warehouse.auth.domain.vo;

import com.warehouse.commonassets.identificator.UserId;

public record ApiKey(UserId userId, String key) {
}
