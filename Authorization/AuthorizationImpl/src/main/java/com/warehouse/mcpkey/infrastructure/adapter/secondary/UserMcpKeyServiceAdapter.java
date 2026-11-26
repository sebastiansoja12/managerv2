package com.warehouse.mcpkey.infrastructure.adapter.secondary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.application.port.secondary.UserMcpKeyServicePort;

public class UserMcpKeyServiceAdapter implements UserMcpKeyServicePort {

    private final UserService userService;

    private final CurrentUserApiService currentUserApiService;

    public UserMcpKeyServiceAdapter(final UserService userService, final CurrentUserApiService currentUserApiService) {
        this.userService = userService;
        this.currentUserApiService = currentUserApiService;
    }

    @Override
    public void requireManageableUser(final UserId userId) {
        final OrganizationUser administrator = this.userService.findUserById(this.currentUserApiService.getCurrentUserId());
        final OrganizationUser target = this.userService.findUserById(userId);
        if (administrator == null || Boolean.TRUE.equals(administrator.isDeleted())
                || !administrator.getLevel().isAtLeast(AccountLevel.ADMIN) || target == null
                || Boolean.TRUE.equals(target.isDeleted())
                || (!administrator.getUserId().equals(userId) && !administrator.canManage(target))) {
            throw new ProblemDetailsException(403, "User MCP access cannot be managed by current user");
        }
    }

    @Override
    public void assignMcpKey(final UserId userId, final McpKeyId mcpKeyId) {
        this.userService.assignMcpKey(userId, mcpKeyId);
    }

    @Override
    public void removeMcpKey(final UserId userId, final McpKeyId mcpKeyId) {
        this.userService.removeMcpKey(userId, mcpKeyId);
    }
}
