package com.warehouse.process.infrastructure.adapter.secondary;

import com.warehouse.auth.UserApiService;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.process.domain.port.secondary.UserServicePort;

public class UserServiceAdapter implements UserServicePort {

    private final UserApiService userApiService;

    public UserServiceAdapter(final UserApiService userApiService) {
        this.userApiService = userApiService;
    }

    @Override
    public String getUsername(final UserId userId) {
        try {
            final UserDto user = userApiService.findById(userId);
            return user != null ? user.username() : null;
        } catch (final RuntimeException e) {
            return null;
        }
    }
}
