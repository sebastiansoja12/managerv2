package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevicePairReadRepository extends JpaRepository<DevicePairEntity, Long> {
}
