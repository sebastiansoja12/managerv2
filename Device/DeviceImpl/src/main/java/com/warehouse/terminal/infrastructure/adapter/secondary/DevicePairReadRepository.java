package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;

@Repository
public interface DevicePairReadRepository extends JpaRepository<DevicePairEntity, Long> {
    Optional<DevicePairEntity> findByDeviceId(final DeviceId deviceId);
    Optional<DevicePairEntity> findByPairKey(final String pairKey);
}
