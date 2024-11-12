package com.warehouse.terminal.domain.service;

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
}
