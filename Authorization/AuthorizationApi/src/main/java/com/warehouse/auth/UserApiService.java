package com.warehouse.auth;

import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.auth.infrastructure.dto.UserIdDto;
import com.warehouse.commonassets.identificator.UserId;

public interface UserApiService {
    UserDto findById(final UserId userId);
    UserDto findByUsername(final String username);
    UserIdDto findInitialUserForOperator();
}
