package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.enumeration.DeviceType;
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
        return this.deviceVersionRepository.find(deviceId).orElse(null);
    }

    @Override
    public void saveOrUpdate(final DeviceType deviceType, final DeviceId deviceId, final String version) {
        final DeviceVersion deviceVersion = new DeviceVersion(deviceType, version, deviceId);
        deviceVersionRepository.saveOrUpdate(deviceVersion);
    }
}
