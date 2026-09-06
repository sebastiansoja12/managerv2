package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.UserApiService;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.auth.infrastructure.dto.UserIdDto;
import com.warehouse.commonassets.identificator.UserId;

public class UserApiServiceAdapter implements UserApiService {

    private final UserService userService;

    private final ResponseMapper responseMapper;

    public UserApiServiceAdapter(final UserService userService, final ResponseMapper responseMapper) {
        this.userService = userService;
        this.responseMapper = responseMapper;
    }

    @Override
    public UserDto findById(final UserId userId) {
        final User user = userService.findUserById(userId);
        return map(user);
    }

    @Override
    public UserDto findByUsername(final String username) {
        final User user = userService.findUser(username);
        return map(user);
    }

    @Override
    public UserIdDto findInitialUserForOperator() {
        final UserId userId = userService.findInitialUser();
        return new UserIdDto(userId.getValue());
    }

    private UserDto map(final User user) {
        if (user == null) {
            return null;
        }
        return responseMapper.map(user, userService.getDepartmentCode(user.getDepartmentId()));
    }
}
