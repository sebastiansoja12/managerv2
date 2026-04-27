package com.warehouse.auth;

import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.UserId;

public interface CurrentUserService {
    UserId getCurrentUserId();
    UserDto getCurrentUser();
}
