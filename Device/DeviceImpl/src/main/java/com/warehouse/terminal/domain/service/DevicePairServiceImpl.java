package com.warehouse.terminal.domain.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.port.secondary.DevicePairRepository;
import com.warehouse.terminal.domain.vo.DevicePairId;

public class DevicePairServiceImpl implements DevicePairService {

    private static final Duration PAIR_KEY_VALIDITY = Duration.ofHours(48);

    private final DevicePairRepository devicePairRepository;

    private final DevicePairKeyService devicePairKeyService;

    public DevicePairServiceImpl(final DevicePairRepository devicePairRepository,
                                 final DevicePairKeyService devicePairKeyService) {
        this.devicePairRepository = devicePairRepository;
        this.devicePairKeyService = devicePairKeyService;
    }

    @Transactional
    @Override
    public DevicePair pairDevice(final Device device) {
        final Instant now = Instant.now();
        final Optional<DevicePair> existingDevicePair = this.devicePairRepository.findByDeviceId(device.getDeviceId());
        return existingDevicePair.map(devicePair -> {
            if (!devicePair.isPaired() || !devicePair.containsValidPairKey(now)) {
                devicePair.pair(generateEncryptedPairKey(device, now), now.plus(PAIR_KEY_VALIDITY));
                this.devicePairRepository.save(devicePair);
            }
            return devicePair;
        }).orElseGet(() -> createDevicePair(device, now));
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

    @Override
    public Optional<DevicePair> findByPairKey(final String pairKey) {
        return this.devicePairRepository.findByPairKey(pairKey);
    }

    @Override
    public Optional<DevicePair> findValidByPairKey(final String pairKey) {
        final Instant now = Instant.now();
        return this.devicePairRepository.findByPairKey(pairKey)
                .filter(devicePair -> devicePair.isPaired() && devicePair.containsValidPairKey(now));
    }

    private DevicePairId nextDevicePairId() {
        return new DevicePairId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }

    private DevicePair createDevicePair(final Device device, final Instant now) {
        final DevicePairId devicePairId = this.nextDevicePairId();
        final DevicePair devicePair = new DevicePair(
                device.getDeviceId(),
                generateEncryptedPairKey(device, now),
                now.plus(PAIR_KEY_VALIDITY),
                devicePairId);
        this.devicePairRepository.save(devicePair);
        return devicePair;
    }

    protected String generateEncryptedPairKey(final Device device, final Instant now) {
        return devicePairKeyService.generateEncryptedPairKey(device.getDeviceId(), now);
    }
}
