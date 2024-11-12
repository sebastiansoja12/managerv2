package com.warehouse.terminal.domain.service;

import java.util.UUID;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.port.secondary.DevicePairRepository;
import com.warehouse.terminal.domain.vo.DevicePairId;

public class DevicePairServiceImpl implements DevicePairService {

    private final DevicePairRepository devicePairRepository;

    public DevicePairServiceImpl(final DevicePairRepository devicePairRepository) {
        this.devicePairRepository = devicePairRepository;
    }

    @Override
    public void pairDevice(final Terminal terminal) {
        final DevicePairId devicePairId = this.nextDevicePairId();
        this.devicePairRepository.pair(terminal, devicePairId);
    }

    @Override
    public void unpairDevice(final DeviceId deviceId) {
        final DevicePair devicePair = this.devicePairRepository.findDevicePairByDeviceId(deviceId);
        devicePair.unpair();
        this.devicePairRepository.update(devicePair);
    }

    @Override
    public DevicePair findByDeviceId(final DeviceId deviceId) {
        return this.devicePairRepository.findDevicePairByDeviceId(deviceId);
    }

    private DevicePairId nextDevicePairId() {
        return new DevicePairId(UUID.randomUUID().getLeastSignificantBits());
    }
}
