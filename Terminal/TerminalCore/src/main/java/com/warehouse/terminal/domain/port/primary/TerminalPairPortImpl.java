package com.warehouse.terminal.domain.port.primary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.service.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TerminalPairPortImpl implements TerminalPairPort {
    
    private final TerminalValidatorService terminalValidatorService;
    
    private final TerminalService terminalService;

    private final UserService userService;

    private final DevicePairService devicePairService;

    private final DeviceVersionService deviceVersionService;

	public TerminalPairPortImpl(final TerminalValidatorService terminalValidatorService,
                                final TerminalService terminalService,
                                final UserService userService,
                                final DevicePairService devicePairService,
                                final DeviceVersionService deviceVersionService) {
		this.terminalValidatorService = terminalValidatorService;
		this.terminalService = terminalService;
        this.userService = userService;
        this.devicePairService = devicePairService;
        this.deviceVersionService = deviceVersionService;
    }

    @Override
    public boolean isConnected(final DeviceId deviceId) {
        final DevicePair devicePair = this.devicePairService.findByDeviceId(deviceId);
        return devicePair != null && devicePair.isPaired();
    }

    @Override
    public boolean isValid(final DeviceId deviceId) {
        final Device device = this.terminalService.findByDeviceId(deviceId);
        final DeviceVersion deviceVersion = this.deviceVersionService.findByDeviceId(deviceId);
        final DevicePair devicePair = this.devicePairService.findByDeviceId(deviceId);
        return device != null && deviceVersion != null && devicePair != null;
    }

    @Override
    public boolean isUserValid(final DeviceId deviceId, final UserId userId) {
        final Device device = this.terminalService.findByDeviceId(deviceId);
        return this.userService.existsByUserId(userId) && device.getUserId().equals(userId);
    }

    @Override
    public boolean isVersionValid(final DeviceId deviceId, final DeviceVersion deviceVersion) {
        final Device device = this.terminalService.findByDeviceId(deviceId);
        return device != null && device.getVersion().equals(deviceVersion.getVersion());
    }

    @Override
    public boolean updateRequired(final DeviceId deviceId, final DeviceVersion version) {
        final DeviceVersion deviceVersion = this.deviceVersionService.findByDeviceId(deviceId);
        return deviceVersion != null && !deviceVersion.getVersion().equals(version.getVersion());
    }

    @Override
    public void pair(final DeviceId deviceId) {
        final Terminal terminal = this.terminalService.findByDeviceId(deviceId);
        log.info("Pairing terminal {}", terminal.getTerminalId());

        this.terminalValidatorService.validateDepartment(terminal.getDepotCode());
        this.terminalValidatorService.validateTerminalVersion(terminal.getTerminalId());
        this.userService.validateUser(terminal.getUserId());
        this.devicePairService.pairDevice(terminal);
    }

    @Override
    public void unpair(final Terminal terminal) {
        this.devicePairService.unpairDevice(terminal.getTerminalId());
    }
}
