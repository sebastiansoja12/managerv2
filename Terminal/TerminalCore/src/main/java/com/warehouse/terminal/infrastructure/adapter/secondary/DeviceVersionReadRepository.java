package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceVersionEntity;

public interface DeviceVersionReadRepository extends JpaRepository<DeviceVersionEntity, Long> {
    Optional<DeviceVersionEntity> findByDeviceId(Long value);
}
