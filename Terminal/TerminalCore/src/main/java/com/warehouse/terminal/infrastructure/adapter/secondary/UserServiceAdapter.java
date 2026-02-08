package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.auth.UserApiService;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.port.secondary.UserServicePort;
import com.warehouse.terminal.domain.vo.User;

public class UserServiceAdapter implements UserServicePort {

    private final UserApiService userApiService;

    public UserServiceAdapter(final UserApiService userApiService) {
        this.userApiService = userApiService;
    }

    @Override
    public User findUserById(final UserId userId) {
        final UserDto user = this.userApiService.findById(userId);
        return User.from(user);
    }
}
