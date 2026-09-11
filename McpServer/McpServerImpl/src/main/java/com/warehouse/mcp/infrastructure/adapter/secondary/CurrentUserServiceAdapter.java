package com.warehouse.mcp.infrastructure.adapter.secondary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcp.application.port.secondary.CurrentUserServicePort;
import com.warehouse.mcp.domain.vo.CurrentUserProfile;

public class CurrentUserServiceAdapter implements CurrentUserServicePort {

    private final CurrentUserApiService currentUserApiService;

    public CurrentUserServiceAdapter(final CurrentUserApiService currentUserApiService) {
        this.currentUserApiService = currentUserApiService;
    }

    @Override
    public CurrentUserProfile currentUser() {
        final UserDto user = this.currentUserApiService.getCurrentUser();
        return new CurrentUserProfile(
                user.userId() != null ? new UserId(user.userId().value()) : null,
                user.username(),
                user.firstName(),
                user.lastName(),
                user.role(),
                user.departmentCode(),
                user.operatorId() != null ? OperatorId.of(user.operatorId().value()) : null);
    }
}
