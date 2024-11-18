package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.terminal.infrastructure.adapter.secondary.entity.UserEntity;

@Repository("device.userReadRepository")
public interface UserReadRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(final String username);
}
