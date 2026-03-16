package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceSettingsReadRepository extends JpaRepository<DeviceSettingsEntity, String> {
    Optional<DeviceSettingsEntity> findByDeviceId(final DeviceId deviceId);
}
