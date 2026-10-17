package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

public abstract class UserToModelMapper {

    public static OrganizationUser map(final UserEntity entity) {
        final OrganizationUser user = OrganizationUser.forRole(UserRoleMapper.map(entity.getRole()));
        user.setUserId(entity.getUserId());
        user.assignOperator(entity.operatorId());
        user.setUsername(entity.getUsername());
        user.setEmail(entity.getEmail());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setPassword(entity.getPassword());
        user.setDepartmentId(entity.getDepartmentId());
        user.setLanguage(entity.getLanguage());
        user.setPermissions(UserPermissionToModelMapper.map(entity.getPermissions()));
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());
        user.setDeleted(entity.isDeleted());
        user.setInitial(entity.getInitial());
        user.setApiKey(entity.getApiKey());
        return user;
    }
}
