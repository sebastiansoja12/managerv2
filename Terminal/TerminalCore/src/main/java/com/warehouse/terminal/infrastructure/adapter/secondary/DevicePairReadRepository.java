package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;

public interface DevicePairReadRepository extends JpaRepository<DevicePairEntity, Long> {
    @Query("SELECT dp FROM DevicePairEntity dp WHERE dp.deviceEntity.id = :deviceId")
    Optional<DevicePairEntity> findByDeviceId(@Param("deviceId") Long deviceId);
}
