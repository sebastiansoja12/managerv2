package com.warehouse.onboarding.application.port.secondary;

import com.warehouse.commonassets.identificator.UserId;

public interface CurrentUserServicePort {
    UserId currentUserId();
}
