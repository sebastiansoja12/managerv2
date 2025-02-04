package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.time.Instant;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.port.secondary.DevicePairRepository;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;
import com.warehouse.terminal.domain.vo.DevicePairId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;

public class DevicePairRepositoryImpl implements DevicePairRepository {
    
    private final DevicePairReadRepository repository;

    private final DeviceRepository deviceRepository;

    public DevicePairRepositoryImpl(final DevicePairReadRepository repository,
                                    final DeviceRepository deviceRepository) {
        this.repository = repository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public void pair(final DevicePair devicePair) {
        final DevicePairEntity devicePairEntity = DevicePairEntity.from(devicePair);
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
        final Optional<DevicePairEntity> devicePairEntity = this.repository.findByDevice_DeviceId(deviceId);
        return devicePairEntity.map(DevicePairId::from).orElseThrow();
    }

    @Override
    public DevicePair findDevicePairByDeviceId(final DeviceId deviceId) {
        final Optional<DevicePairEntity> devicePairEntity = this.repository.findByDevice_DeviceId(deviceId);
        return devicePairEntity.map(DevicePair::from).orElse(new DevicePair(false));
    }

    @Override
    public void update(final DevicePair devicePair) {
        final DevicePairEntity deviceEntity = DevicePairEntity.from(devicePair);
        this.repository.save(deviceEntity);
    }

    @Override
    public Optional<DevicePair> findByDeviceId(final DeviceId deviceId) {
        final Optional<DevicePairEntity> devicePairEntity = this.repository.findByDevice_DeviceId(deviceId);
        return devicePairEntity.map(DevicePair::from);
    }

    @Override
    public void save(final DevicePair devicePair) {
        final DevicePairEntity devicePairEntity = DevicePairEntity.from(devicePair, (Terminal) deviceRepository.findById(devicePair.getDeviceId()));
        this.repository.save(devicePairEntity);
    }
}
