package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.Optional;
import java.util.UUID;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DeviceSettings;
import com.warehouse.terminal.domain.port.secondary.DeviceSettingsRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceSettingsEntity;

public class DeviceSettingsRepositoryImpl implements DeviceSettingsRepository {

    private final DeviceSettingsReadRepository repository;

    public DeviceSettingsRepositoryImpl(final DeviceSettingsReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public DeviceSettings getDeviceSettings(final DeviceId deviceId) {
        final Optional<DeviceSettingsEntity> deviceSettingsEntity = repository.findByDeviceId(deviceId);
        return deviceSettingsEntity.map(DeviceSettings::from).orElse(DeviceSettings.empty());
    }

    @Override
    public void saveOrUpdate(final DeviceSettings deviceSettings) {
        this.repository.findByDeviceId(deviceSettings.getDeviceId())
                .map(settings -> DeviceSettingsEntity.from(deviceSettings, settings.getDeviceSettingsId()))
                .orElseGet(() -> this.repository.save(DeviceSettingsEntity.from(deviceSettings, deviceSettingsId())));
    }

    private String deviceSettingsId() {
        return UUID.randomUUID().toString();
    }
}
