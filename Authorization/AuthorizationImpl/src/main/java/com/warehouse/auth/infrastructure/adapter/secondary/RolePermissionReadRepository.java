package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolePermissionReadRepository extends JpaRepository<RolePermissionEntity, Long> {
    Optional<RolePermissionEntity> findByName(final String name);
}
