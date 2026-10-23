package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.time.Instant;
import java.util.Optional;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.DeviceActivationCode;
import com.warehouse.terminal.domain.port.secondary.DeviceActivationCodeRepository;
import com.warehouse.terminal.domain.vo.DeviceActivationCodeId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceActivationCodeEntity;

public class DeviceActivationCodeRepositoryImpl implements DeviceActivationCodeRepository {

    private final DeviceActivationCodeReadRepository repository;

    public DeviceActivationCodeRepositoryImpl(final DeviceActivationCodeReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(final DeviceActivationCode activationCode) {
        this.repository.save(toEntity(activationCode));
    }

    @Override
    public Optional<DeviceActivationCode> findByCodeHash(final String codeHash) {
        return this.repository.findByCodeHash(codeHash).map(this::toModel);
    }

    @Override
    public boolean markUsed(final DeviceActivationCodeId activationCodeId, final Instant usedAt) {
        return this.repository.markUsed(activationCodeId.value(), usedAt) > 0;
    }

    @Override
    public void deleteUnusedByDeviceId(final DeviceId deviceId) {
        this.repository.deleteUnusedByDeviceId(deviceId.value());
    }

    @Override
    public void deleteUnusedByUserId(final UserId userId) {
        this.repository.deleteUnusedByUserId(userId.value());
    }

    private DeviceActivationCodeEntity toEntity(final DeviceActivationCode activationCode) {
        return new DeviceActivationCodeEntity(
                activationCode.getActivationCodeId().value(),
                activationCode.getDeviceId(),
                activationCode.getUserId() != null ? activationCode.getUserId().value() : null,
                activationCode.getOperatorId() != null ? activationCode.getOperatorId().value() : null,
                activationCode.getDepartmentCode() != null ? activationCode.getDepartmentCode().value() : null,
                activationCode.getCodeHash(),
                activationCode.getCreatedAt(),
                activationCode.getExpiresAt(),
                activationCode.getUsedAt());
    }

    private DeviceActivationCode toModel(final DeviceActivationCodeEntity entity) {
        return new DeviceActivationCode(
                new DeviceActivationCodeId(entity.getActivationCodeId()),
                entity.getDeviceId(),
                entity.getUserId() != null ? new UserId(entity.getUserId()) : null,
                entity.getOperatorId() != null ? OperatorId.of(entity.getOperatorId()) : null,
                entity.getDepartmentCode() != null ? new DepartmentCode(entity.getDepartmentCode()) : null,
                entity.getCodeHash(),
                entity.getCreatedAt(),
                entity.getExpiresAt(),
                entity.getUsedAt());
    }
}
