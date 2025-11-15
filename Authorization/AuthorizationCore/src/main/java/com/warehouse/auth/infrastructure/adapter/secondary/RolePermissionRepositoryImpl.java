package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.model.RolePermission;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.RolePermissionRepository;
import com.warehouse.auth.domain.vo.RolePermissionId;

import java.util.Set;

public class RolePermissionRepositoryImpl implements RolePermissionRepository {
    
    private final RolePermissionReadRepository rolePermissionReadRepository;
    
    public RolePermissionRepositoryImpl(final RolePermissionReadRepository rolePermissionReadRepository) {
        this.rolePermissionReadRepository = rolePermissionReadRepository;
    }
    
	@Override
	public RolePermission findByName(final String name) {
		return rolePermissionReadRepository.findByName(name)
                .map(role -> new RolePermission(new RolePermissionId(role.getId()), User.Permission.valueOf(role.getName())))
				.orElse(null);
	}

    @Override
    public Set<RolePermission> findAllAdminPermissions() {
		final Set<RolePermission> rolePermissions = rolePermissionReadRepository.findAll().stream().map(
				role -> new RolePermission(new RolePermissionId(role.getId()), User.Permission.valueOf(role.getName())))
				.collect(java.util.stream.Collectors.toSet());
        return rolePermissions.stream().filter(RolePermission::isAdmin).collect(java.util.stream.Collectors.toSet());
    }
}
