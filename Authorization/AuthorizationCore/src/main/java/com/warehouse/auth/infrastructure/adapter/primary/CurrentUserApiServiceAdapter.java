package com.warehouse.auth.infrastructure.adapter.primary;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;

public class CurrentUserApiServiceAdapter implements CurrentUserApiService {

    private final UserService userService;

    public CurrentUserApiServiceAdapter(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserId getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernameTenantPasswordAuthenticationToken token) {
            return (UserId) token.getPrincipal();
        } else {
            throw new IllegalStateException("No user found");
        }
    }

    @Override
    public UserDto getCurrentUser() {
        final UserId userId = getCurrentUserId();
        final User user = userService.findUserById(userId);
        return ResponseMapper.map(user);
    }
}
