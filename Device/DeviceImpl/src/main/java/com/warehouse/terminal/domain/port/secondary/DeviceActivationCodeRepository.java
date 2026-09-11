package com.warehouse.terminal.domain.port.secondary;

import java.time.Instant;
import java.util.Optional;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.DeviceActivationCode;
import com.warehouse.terminal.domain.vo.DeviceActivationCodeId;

public interface DeviceActivationCodeRepository {
    void save(final DeviceActivationCode activationCode);
    Optional<DeviceActivationCode> findByCodeHash(final String codeHash);
    boolean markUsed(final DeviceActivationCodeId activationCodeId, final Instant usedAt);
    void deleteUnusedByDeviceId(final DeviceId deviceId);
    void deleteUnusedByUserId(final UserId userId);
}
