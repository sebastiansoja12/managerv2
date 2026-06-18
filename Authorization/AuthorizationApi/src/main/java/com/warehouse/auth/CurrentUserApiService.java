package com.warehouse.auth;

import com.warehouse.auth.infrastructure.dto.CurrentUserAuthenticationDto;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.UserId;

public interface CurrentUserApiService {
    UserId getCurrentUserId();
    UserDto getCurrentUser();
    CurrentUserAuthenticationDto getCurrentUserAuthentication();
}
