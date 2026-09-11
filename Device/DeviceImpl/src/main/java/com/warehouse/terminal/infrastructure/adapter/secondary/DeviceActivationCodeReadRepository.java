package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceActivationCodeEntity;

@Repository
public interface DeviceActivationCodeReadRepository extends JpaRepository<DeviceActivationCodeEntity, UUID> {
    Optional<DeviceActivationCodeEntity> findByCodeHash(final String codeHash);

    @Modifying
    @Query("update DeviceActivationCodeEntity activationCode set activationCode.usedAt = :usedAt "
            + "where activationCode.activationCodeId = :activationCodeId and activationCode.usedAt is null")
    int markUsed(@Param("activationCodeId") final UUID activationCodeId, @Param("usedAt") final Instant usedAt);

    @Modifying
    @Query("delete from DeviceActivationCodeEntity activationCode "
            + "where activationCode.deviceId.value = :deviceId and activationCode.usedAt is null")
    void deleteUnusedByDeviceId(@Param("deviceId") final String deviceId);

    @Modifying
    @Query("delete from DeviceActivationCodeEntity activationCode "
            + "where activationCode.userId = :userId and activationCode.usedAt is null")
    void deleteUnusedByUserId(@Param("userId") final Long userId);
}
