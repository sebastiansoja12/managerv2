package com.warehouse.terminal.domain.port.primary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.PairStatus;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.model.request.DevicePairRequest;
import com.warehouse.terminal.domain.model.response.DevicePairResponse;
import com.warehouse.terminal.domain.service.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DevicePairPortImpl implements DevicePairPort {
    
    private final TerminalValidatorService terminalValidatorService;
    
    private final TerminalService terminalService;

    private final UserService userService;

    private final DevicePairService devicePairService;

    private final DeviceVersionService deviceVersionService;

	public DevicePairPortImpl(final TerminalValidatorService terminalValidatorService,
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
    public boolean isPaired(final DeviceId deviceId) {
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
        return this.userService.existsByUserId(userId) && device != null && device.getUserId().equals(userId);
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
    public DevicePairResponse pair(final DevicePairRequest request) {
        final Terminal terminal = this.terminalService.findByDeviceId(request.getDeviceId());
        log.info("Pairing terminal [{}]", terminal.getTerminalId().getValue());
        final DeviceId deviceId = terminal.getDeviceId();
        this.terminalValidatorService.validateDepartment(terminal.getDepartmentCode());
        this.terminalValidatorService.validateTerminalVersion(deviceId);
        final Boolean userValid = this.userService.existsByUserId(terminal.getUserId())
                && this.userService.existsByUserId(request.getUserId());

        final DeviceVersion deviceVersion = new DeviceVersion(terminal.getVersion(), terminal.getTerminalId());
        final Boolean deviceUpToDate = !updateRequired(deviceId, deviceVersion);
        if (deviceUpToDate && userValid && terminal.isActive()) {
            this.devicePairService.pairDevice(terminal);
        }
        final DevicePair devicePair = this.devicePairService.findByDeviceId(request.getDeviceId());
		return new DevicePairResponse(request.getUserId(), request.getDeviceId(), devicePair.getDevicePairId(),
				devicePair.isPaired() ? PairStatus.PAIRED.name() : PairStatus.UNPAIRED.name(), devicePair.isPaired() ?
                devicePair.getPairKey() : StringUtils.EMPTY, userValid, terminal.isActive(),
				deviceUpToDate);
    }

    @Override
    public void unpair(final Terminal terminal) {
        this.devicePairService.unpairDevice(terminal.getTerminalId());
    }
}
