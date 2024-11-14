package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.time.Instant;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.port.secondary.DevicePairRepository;
import com.warehouse.terminal.domain.vo.DevicePairId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;

public class DevicePairRepositoryImpl implements DevicePairRepository {
    
    private final DevicePairReadRepository repository;
    private final String alreadyPairedExceptionMessage = "Device %s already paired";

    public DevicePairRepositoryImpl(final DevicePairReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void pair(final Terminal terminal, final DevicePairId devicePairId) {
        final DeviceEntity deviceEntity = DeviceEntity.from(terminal);
        validateNotPaired(terminal.getTerminalId());
		final DevicePairEntity devicePairEntity = new DevicePairEntity(devicePairId, deviceEntity, true, Instant.now(),
				StringUtils.EMPTY, "pairKey");
        this.repository.save(devicePairEntity);
    }

    @Override
    public void unpair(final Terminal terminal, final DevicePairId devicePairId) {
        final DeviceEntity deviceEntity = DeviceEntity.from(terminal);
        final DevicePairEntity devicePairEntity = new DevicePairEntity(devicePairId, deviceEntity, false, Instant.now(),
                StringUtils.EMPTY, null);
        this.repository.save(devicePairEntity);
    }

    @Override
    public DevicePairId findPairIdByDeviceId(final DeviceId deviceId) {
        final Optional<DevicePairEntity> devicePairEntity = this.repository.findById(deviceId.getValue());
        return devicePairEntity.map(DevicePairId::from).orElseThrow();
    }

    @Override
    public DevicePair findDevicePairByDeviceId(final DeviceId deviceId) {
        final Optional<DevicePairEntity> devicePairEntity = this.repository.findByDeviceId(deviceId.getValue().toString());
        return devicePairEntity.map(DevicePair::from).orElseThrow();
    }

    @Override
    public void update(final DevicePair devicePair) {
        final DevicePairEntity deviceEntity = DevicePairEntity.from(devicePair);
        this.repository.save(deviceEntity);
    }

    private void validateNotPaired(final DeviceId deviceId) {
        final Boolean devicePaired = this.repository
                .findByDeviceId(deviceId.getValue().toString())
                .map(DevicePairEntity::isPaired)
                .orElse(Boolean.FALSE);
        if (devicePaired) {
            throw new RuntimeException(String.format(alreadyPairedExceptionMessage, deviceId));
        }
    }
}
