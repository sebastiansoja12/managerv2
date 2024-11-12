package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DevicePairReadRepository extends JpaRepository<DevicePairEntity, Long> {
    Optional<DevicePairEntity> findByDeviceId(final String deviceId);
}
