package com.warehouse.process.domain.port.secondary;

import com.warehouse.commonassets.identificator.UserId;

public interface CurrentUserServicePort {
    UserId getCurrentUserId();
}
