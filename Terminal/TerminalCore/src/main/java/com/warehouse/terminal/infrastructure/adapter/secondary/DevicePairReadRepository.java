package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;

public interface DevicePairReadRepository extends JpaRepository<DevicePairEntity, Long> {
    Optional<DevicePairEntity> findByDevice_DeviceId(final DeviceId deviceId);
}
