package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.port.secondary.DeviceVersionRepository;

public class DeviceVersionServiceImpl implements DeviceVersionService {

    private final DeviceVersionRepository deviceVersionRepository;

    public DeviceVersionServiceImpl(final DeviceVersionRepository deviceVersionRepository) {
        this.deviceVersionRepository = deviceVersionRepository;
    }

    @Override
    public DeviceVersion findByDeviceId(final DeviceId deviceId) {
        return deviceVersionRepository.find(deviceId);
    }

    @Override
    public void saveOrUpdate(final DeviceId deviceId, final String version) {
        final DeviceVersion deviceVersion = this.deviceVersionRepository.find(deviceId);
        deviceVersion.updateVersion(version);
        this.deviceVersionRepository.saveOrUpdate(deviceVersion);
    }
}
