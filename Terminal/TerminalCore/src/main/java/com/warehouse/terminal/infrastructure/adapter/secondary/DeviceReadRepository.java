package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceReadRepository extends JpaRepository<DeviceEntity, DeviceId> {
}
