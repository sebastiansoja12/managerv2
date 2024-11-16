package com.warehouse.terminal.domain.service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;

public class TerminalServiceImpl implements TerminalService {

    private final DeviceRepository deviceRepository;

	public TerminalServiceImpl(final DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public void createTerminal(final Terminal terminal) {
        terminal.setDeviceId(nextDeviceId());
        this.deviceRepository.saveOrUpdate(terminal);
    }

    @Override
    public void changeDeviceType(final DeviceId deviceId, final DeviceType deviceType) {
        final Terminal terminal = (Terminal) this.deviceRepository.findById(deviceId);
        terminal.updateDeviceType(deviceType);
        this.deviceRepository.saveOrUpdate(terminal);
    }

    @Override
    public void assignUser(final DeviceId deviceId, final UserId userId) {
        final Terminal terminal = (Terminal) this.deviceRepository.findById(deviceId);
        terminal.assignUser(userId);
        this.deviceRepository.saveOrUpdate(terminal);
    }

    @Override
    public void updateVersion(final DeviceId deviceId, final String version) {
        final Terminal terminal = (Terminal) this.deviceRepository.findById(deviceId);
        terminal.updateVersion(version);
        this.deviceRepository.saveOrUpdate(terminal);
    }

    @Override
    public Terminal findByDeviceId(final DeviceId deviceId) {
        return (Terminal) this.deviceRepository.findById(deviceId);
    }

    @Override
    public List<Terminal> findAll() {
        return this.deviceRepository.findAll();
    }

    private DeviceId nextDeviceId() {
        final long timestamp = System.currentTimeMillis();
        final long randomPart = ThreadLocalRandom.current().nextLong(1_000L, 10_000L);
        return new DeviceId(timestamp * 10_000 + randomPart);
    }
}
