package com.warehouse.terminal.domain.service;

import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.port.secondary.DevicePairRepository;
import com.warehouse.terminal.domain.vo.DevicePairId;

import java.util.UUID;

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

    private DevicePairId nextDevicePairId() {
        return new DevicePairId(UUID.randomUUID().getLeastSignificantBits());
    }
}
