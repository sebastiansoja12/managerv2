package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.UserApiService;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.UserId;

public class UserApiServiceAdapter implements UserApiService {

    private final UserService userService;

    public UserApiServiceAdapter(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDto findById(final UserId userId) {
        final User user = userService.findUserById(userId);
        return ResponseMapper.map(user);
    }

    @Override
    public UserDto findByUsername(final String username) {
        final User user = userService.findUser(username);
        return ResponseMapper.map(user);
    }
}
