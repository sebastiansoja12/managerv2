package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.commonassets.enumeration.DeviceType;
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
    public void changeDeviceType(final TerminalId terminalId, final DeviceType deviceType) {
        final Terminal terminal = (Terminal) this.deviceRepository.findById(terminalId);
        terminal.updateDeviceType(deviceType);
        this.deviceRepository.saveOrUpdate(terminal);
    }

    @Override
    public void updateVersion(final TerminalId terminalId, final String version) {
        final Terminal terminal = (Terminal) this.deviceRepository.findById(terminalId);
        terminal.updateVersion(version);
        this.deviceRepository.saveOrUpdate(terminal);
    }

    @Override
    public Terminal findByTerminalId(final TerminalId terminalId) {
        return (Terminal) this.deviceRepository.findById(terminalId);
    }
}
