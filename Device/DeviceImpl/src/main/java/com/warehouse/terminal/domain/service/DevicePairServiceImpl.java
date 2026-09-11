package com.warehouse.terminal.domain.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.port.secondary.DevicePairRepository;
import com.warehouse.terminal.domain.vo.DevicePairId;
import com.warehouse.terminal.domain.vo.DevicePairKey;
import com.warehouse.terminal.domain.vo.IssuedDevicePairKey;

public class DevicePairServiceImpl implements DevicePairService {

    private final DevicePairRepository devicePairRepository;

    private final DevicePairKeyService devicePairKeyService;

    private final Duration activatedPairKeyValidity;

    private final Duration operatorSessionValidity;

    public DevicePairServiceImpl(final DevicePairRepository devicePairRepository,
                                 final DevicePairKeyService devicePairKeyService,
                                 final Duration activatedPairKeyValidity,
                                 final Duration operatorSessionValidity) {
        this.devicePairRepository = devicePairRepository;
        this.devicePairKeyService = devicePairKeyService;
        this.activatedPairKeyValidity = activatedPairKeyValidity;
        this.operatorSessionValidity = operatorSessionValidity;
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
        return this.devicePairRepository.findByPairKey(new DevicePairKey(pairKey).hash());
    }

    @Override
    public Optional<DevicePair> findValidByPairKey(final String pairKey) {
        final Instant now = Instant.now();
        return findByPairKey(pairKey)
                .filter(devicePair -> devicePair.isPaired() && devicePair.containsValidPairKey(now));
    }

    @Transactional
    @Override
    public IssuedDevicePairKey activateDevice(final Device device) {
        final Instant now = Instant.now();
        final DevicePairKey pairKey = new DevicePairKey(generateEncryptedPairKey(device, now));
        final Instant pairKeyExpiresAt = now.plus(activatedPairKeyValidity);
        final DevicePair devicePair = this.devicePairRepository.findByDeviceId(device.getDeviceId())
                .map(existing -> {
                    existing.activate(pairKey.hash(), pairKeyExpiresAt);
                    return existing;
                })
                .orElseGet(() -> new DevicePair(device.getDeviceId(), pairKey.hash(), pairKeyExpiresAt,
                        nextDevicePairId()));
        devicePair.markSeen(now);
        this.devicePairRepository.save(devicePair);
        return new IssuedDevicePairKey(pairKey, pairKeyExpiresAt);
    }

    @Override
    public DevicePair startOperatorSession(final DevicePair devicePair, final UserId userId) {
        final Instant now = Instant.now();
        devicePair.startOperatorSession(userId, now.plus(operatorSessionValidity));
        devicePair.markSeen(now);
        this.devicePairRepository.update(devicePair);
        return devicePair;
    }

    @Override
    public void endOperatorSession(final DevicePair devicePair, final UserId userId) {
        if (devicePair.isOperatorSessionOwnedBy(userId)) {
            devicePair.endOperatorSession();
            this.devicePairRepository.update(devicePair);
        }
    }

    @Override
    public void markSeen(final DevicePair devicePair) {
        devicePair.markSeen(Instant.now());
        this.devicePairRepository.update(devicePair);
    }

    private DevicePairId nextDevicePairId() {
        return new DevicePairId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }

    protected String generateEncryptedPairKey(final Device device, final Instant now) {
        return devicePairKeyService.generateEncryptedPairKey(device.getDeviceId(), now);
    }

    @Override
    public Optional<DevicePair> findExistingByDeviceId(final DeviceId deviceId) {
        return this.devicePairRepository.findByDeviceId(deviceId);
    }

    @Override
    public boolean revokePairing(final DeviceId deviceId) {
        return this.devicePairRepository.findByDeviceId(deviceId)
                .map(devicePair -> {
                    devicePair.unpair();
                    devicePair.setPairKeyExpiresAt(Instant.now());
                    this.devicePairRepository.update(devicePair);
                    return true;
                })
                .orElse(false);
    }
}
