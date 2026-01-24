package com.warehouse.terminal.domain.port.primary;

import java.time.Instant;
import java.util.List;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.exception.UserNotFoundException;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.command.DeviceCreateCommand;
import com.warehouse.terminal.domain.model.command.DeviceSettingsRequest;
import com.warehouse.terminal.domain.model.device.Device;
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.service.DeviceGenericService;
import com.warehouse.terminal.domain.service.DeviceVersionService;
import com.warehouse.terminal.domain.service.UserService;
import com.warehouse.terminal.domain.vo.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DevicePortImpl implements DevicePort {

    private final DeviceGenericService deviceGenericService;

    private final UserService userService;

    private final DeviceVersionService deviceVersionService;

    public DevicePortImpl(final DeviceGenericService deviceGenericService,
                          final UserService userService,
                          final DeviceVersionService deviceVersionService) {
        this.deviceGenericService = deviceGenericService;
        this.userService = userService;
        this.deviceVersionService = deviceVersionService;
    }

    @Override
    public DeviceCreateResult create(final DeviceCreateCommand command) {
        logTerminalCreate(command);
        final UserId userId = command.getUserId();
        final DeviceType deviceType = command.getDeviceType();
        final User user = this.userService.findByUserId(command.getUserId());
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        final DeviceId deviceId = this.deviceGenericService.nextDeviceId(deviceType);
        final IdentityInfo deviceIdentityInfo = new IdentityInfo();
        final HardwareProfile hardwareProfile = new HardwareProfile();
        final SoftwareProfile softwareProfile = new SoftwareProfile();
        final NetworkProfile networkProfile = new NetworkProfile();
        final LocationProfile locationProfile = new LocationProfile();
		final OwnershipProfile ownershipProfile = new OwnershipProfile(Instant.now(), null, command.getDepartmentCode(),
				null, null, null, userId, null);
        
		final Device device;
		if (deviceType.equals(DeviceType.TERMINAL)) {
			device = new Terminal(deviceId, DeviceStatus.ACTIVE, deviceIdentityInfo, hardwareProfile, softwareProfile,
					networkProfile, locationProfile, ownershipProfile);
		} else if (deviceType.equals(DeviceType.SCANNER)) {
			device = new Scanner(deviceId, DeviceStatus.ACTIVE, deviceIdentityInfo, hardwareProfile, softwareProfile,
                    networkProfile, locationProfile, ownershipProfile);
		} else {
			device = new Mobile(deviceId, DeviceStatus.ACTIVE, deviceIdentityInfo, hardwareProfile, softwareProfile,
                    networkProfile, locationProfile, ownershipProfile);
		}
        this.deviceGenericService.create(device);

        return new DeviceCreateResult(device.getExternalDeviceId());
    }

    @Override
    public void changeDeviceTypeTo(final DeviceTypeChangeCommand request) {
        final DeviceId deviceId = request.getDeviceId();
        final DeviceType deviceType = request.getDeviceType();
        this.deviceGenericService.changeDeviceType(deviceId, deviceType);
    }

    @Override
    public void changeUserTo(final DeviceUserRequest request) {
        final DeviceId deviceId = request.deviceId();
        final Username username = request.username();
        final Boolean userExists = this.userService.existsByUsername(username);
        if (!userExists) {
            throw new UserNotFoundException(username);
        }
        this.deviceGenericService.assignUser(deviceId, null);
    }

    @Override
    public void changeVersionTo(final DeviceVersionRequest request) {
        final DeviceId deviceId = request.deviceId();
        final String version = request.version();
        this.deviceGenericService.updateVersion(deviceId, version);
    }

    @Override
    public void updateSettings(final DeviceSettingsRequest request) {
        final Device device = deviceGenericService.findByDeviceId(request.getDeviceId());
        if (device != null) {
            this.deviceGenericService.updateSettings(request);
        }
    }

    @Override
    public List<Terminal> allDevices() {
        return this.deviceGenericService.findAll();
    }

    @Override
    public Terminal getDevice(final DeviceId deviceId) {
        return this.deviceGenericService.findByDeviceId(deviceId);
    }

    private void logTerminalCreate(final DeviceCreateCommand request) {
        log.info("Creating terminal device for user {}", request.getUserId());
    }
}
