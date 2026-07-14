package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

public abstract class UserToModelMapper {

    public static User map(final UserEntity entity) {
        final User user = new User();
        user.setUserId(entity.getUserId());
        user.assignOperator(entity.operatorId());
        user.setUsername(entity.getUsername());
        user.setEmail(entity.getEmail());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setPassword(entity.getPassword());
        user.setRole(UserRoleMapper.map(entity.getRole()));
        user.setDepartmentCode(entity.getDepartmentCode());
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
