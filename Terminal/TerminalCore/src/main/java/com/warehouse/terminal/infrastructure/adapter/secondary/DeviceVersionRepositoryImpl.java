package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.port.secondary.DeviceVersionRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceVersionEntity;

public class DeviceVersionRepositoryImpl implements DeviceVersionRepository {

    private final DeviceReadRepository deviceReadRepository;

    private final DeviceVersionReadRepository deviceVersionReadRepository;

    public DeviceVersionRepositoryImpl(final DeviceReadRepository deviceReadRepository,
                                       final DeviceVersionReadRepository deviceVersionReadRepository) {
        this.deviceReadRepository = deviceReadRepository;
        this.deviceVersionReadRepository = deviceVersionReadRepository;
    }

    @Override
    public boolean updateRequired(final DeviceId deviceId) {
        final DeviceEntity deviceEntity = this.deviceReadRepository
                .findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        final DeviceVersionEntity deviceVersionEntity = this.deviceVersionReadRepository
                .findByDeviceId(deviceId.getValue().toString())
                .orElseThrow(() -> new RuntimeException("Device version not found"));
        return !deviceVersionEntity.getVersion().equals(deviceEntity.getVersion());
    }

    @Override
    public DeviceVersion find(final DeviceId deviceId) {
        return this.deviceVersionReadRepository
                .findByDeviceId(deviceId.getValue().toString())
                .map(DeviceVersion::from)
                .orElse(null);
    }

    @Override
    public void saveOrUpdate(final DeviceVersion deviceVersion) {
        final DeviceVersionEntity deviceVersionEntity = DeviceVersionEntity.from(deviceVersion);
        this.deviceVersionReadRepository.saveAndFlush(deviceVersionEntity);
    }
}
