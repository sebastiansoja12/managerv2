package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.CurrentUserAuthenticationPort;
import com.warehouse.auth.domain.vo.CurrentUserAuthentication;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.auth.infrastructure.dto.CurrentUserAuthenticationDto;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.UserId;

public class CurrentUserApiServiceAdapter implements CurrentUserApiService {

    private final CurrentUserAuthenticationPort currentUserAuthenticationPort;

    public CurrentUserApiServiceAdapter(final CurrentUserAuthenticationPort currentUserAuthenticationPort) {
        this.currentUserAuthenticationPort = currentUserAuthenticationPort;
    }

    @Override
    public UserId getCurrentUserId() {
        return currentUserAuthenticationPort.getCurrentUserId();
    }

    @Override
    public UserDto getCurrentUser() {
        final User user = currentUserAuthenticationPort.getCurrentUser();
        return ResponseMapper.map(user);
    }

    @Override
    public CurrentUserAuthenticationDto getCurrentUserAuthentication() {
        final CurrentUserAuthentication authentication = currentUserAuthenticationPort.getCurrentUserAuthentication();
        return new CurrentUserAuthenticationDto(
                authentication.jwtToken(),
                ResponseMapper.map(authentication.user())
        );
    }
}
