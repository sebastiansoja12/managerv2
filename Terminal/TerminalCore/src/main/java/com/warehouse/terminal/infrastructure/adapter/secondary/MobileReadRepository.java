package com.warehouse.terminal.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.MobileEntity;

public interface MobileReadRepository extends JpaRepository<MobileEntity, DeviceId> {
}
