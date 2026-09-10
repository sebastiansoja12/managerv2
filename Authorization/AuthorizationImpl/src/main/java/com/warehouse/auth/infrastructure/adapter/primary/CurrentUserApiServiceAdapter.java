package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.CurrentUserAuthenticationPort;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.domain.vo.CurrentUserAuthentication;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.auth.infrastructure.dto.CurrentUserAuthenticationDto;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.UserId;

public class CurrentUserApiServiceAdapter implements CurrentUserApiService {

    private final CurrentUserAuthenticationPort currentUserAuthenticationPort;

    private final ResponseMapper responseMapper;

    private final UserService userService;

    public CurrentUserApiServiceAdapter(final CurrentUserAuthenticationPort currentUserAuthenticationPort,
                                        final ResponseMapper responseMapper,
                                        final UserService userService) {
        this.currentUserAuthenticationPort = currentUserAuthenticationPort;
        this.responseMapper = responseMapper;
        this.userService = userService;
    }

    @Override
    public UserId getCurrentUserId() {
        return currentUserAuthenticationPort.getCurrentUserId();
    }

    @Override
    public UserDto getCurrentUser() {
        final User user = currentUserAuthenticationPort.getCurrentUser();
        return responseMapper.map(user, userService.getDepartmentCode(user.getDepartmentId()));
    }

    @Override
    public CurrentUserAuthenticationDto getCurrentUserAuthentication() {
        final CurrentUserAuthentication authentication = currentUserAuthenticationPort.getCurrentUserAuthentication();
        return new CurrentUserAuthenticationDto(
                authentication.jwtToken(),
                responseMapper.map(authentication.user(),
                        userService.getDepartmentCode(authentication.user().getDepartmentId()))
        );
    }
}
