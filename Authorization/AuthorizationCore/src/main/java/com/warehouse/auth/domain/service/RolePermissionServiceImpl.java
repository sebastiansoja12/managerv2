package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.RolePermission;
import com.warehouse.auth.domain.port.secondary.RolePermissionRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionServiceImpl(final RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public RolePermission findByName(final String name) {
        return rolePermissionRepository.findByName(name);
    }

    @Override
    public Set<RolePermission> findAllAdminPermissions() {
        return rolePermissionRepository.findAllAdminPermissions();
    }
}
