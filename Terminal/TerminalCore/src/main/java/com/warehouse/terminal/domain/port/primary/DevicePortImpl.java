package com.warehouse.terminal.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.exception.UserNotFoundException;
import com.warehouse.terminal.domain.model.Department;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DeviceSettings;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.command.DeviceCreateCommand;
import com.warehouse.terminal.domain.model.command.DeviceSettingsRequest;
import com.warehouse.terminal.domain.model.command.DeviceUpdateCommand;
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.port.secondary.DepartmentServicePort;
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

    private final DepartmentServicePort departmentServicePort;

    public DevicePortImpl(final DeviceGenericService deviceGenericService,
                          final UserService userService,
                          final DeviceVersionService deviceVersionService,
                          final DepartmentServicePort departmentServicePort) {
        this.deviceGenericService = deviceGenericService;
        this.userService = userService;
        this.deviceVersionService = deviceVersionService;
        this.departmentServicePort = departmentServicePort;
    }

    @Override
    public DeviceCreateResult create(final DeviceCreateCommand command) {
        logDeviceCreation(command);
        final UserId userId = command.getUserId();
        final DeviceType deviceType = command.getDeviceType();
        final DepartmentCode departmentCode = command.getDepartmentCode();
        final User user = this.userService.findByUserId(command.getUserId());
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        final Department department = this.departmentServicePort.getDepartment(departmentCode);
        final DeviceId deviceId = this.deviceGenericService.nextDeviceId(deviceType);
        final IdentityInfo deviceIdentityInfo = command.getIdentity();
        final HardwareProfile hardwareProfile = command.getHardware();
        final SoftwareProfile softwareProfile = command.getSoftware();
        final NetworkProfile networkProfile = command.getNetwork();
        final LocationProfile locationProfile = LocationProfile.initializeLocation(department.getCoordinates(),
                "", "", false);
		final OwnershipProfile ownershipProfile = OwnershipProfile.initializeOwnership("", userId, departmentCode,
                null);
        
		final Device device;
		if (deviceType.equals(DeviceType.TERMINAL)) {
			device = new Terminal(deviceId, DeviceStatus.ACTIVE, deviceIdentityInfo, hardwareProfile, softwareProfile,
					networkProfile, locationProfile, ownershipProfile);
		} else if (deviceType.equals(DeviceType.SCANNER)) {
			device = new Scanner(deviceId, deviceIdentityInfo, hardwareProfile, networkProfile, ownershipProfile,
                    command.getScanType(), command.getScannerType());
		} else {
			device = new Mobile(deviceId, DeviceStatus.ACTIVE, deviceIdentityInfo, hardwareProfile, softwareProfile,
                    networkProfile, locationProfile, ownershipProfile);
		}
        this.deviceGenericService.create(device);

        return new DeviceCreateResult(deviceId);
    }

    @Override
    public void changeUserTo(final DeviceUserRequest request) {
        final DeviceId deviceId = request.deviceId();
        final Username username = request.username();
        final User user = this.userService.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        this.deviceGenericService.assignUser(deviceId, user.userId());
    }

    @Override
    public void changeVersionTo(final DeviceVersionRequest request) {
        final DeviceId deviceId = request.deviceId();
        final String version = request.version();
        this.deviceGenericService.updateVersion(deviceId, version);
    }

    @Override
    public void updateDevice(final DeviceUpdateCommand request) {
        validateUpdateReferences(request);
        this.deviceGenericService.updateDevice(request);
    }

    @Override
    public void updateSettings(final DeviceSettingsRequest request) {
        this.deviceGenericService.updateSettings(request.getDeviceId(), DeviceSettings.from(request));
    }

    @Override
    public List<Device> allDevices() {
        return this.deviceGenericService.findAll();
    }

    @Override
    public Device getDevice(final DeviceId deviceId) {
        return this.deviceGenericService.findByDeviceId(deviceId);
    }

    private void logDeviceCreation(final DeviceCreateCommand request) {
        log.info("Creating device for user {}", request.getUserId());
    }

    private void validateUpdateReferences(final DeviceUpdateCommand request) {
        if (request.userId() != null) {
            validateUser(request.userId());
        }
        if (request.departmentCode() != null) {
            this.departmentServicePort.getDepartment(request.departmentCode());
        }
        if (request.ownership() != null) {
            if (request.ownership().getUserId() != null) {
                validateUser(request.ownership().getUserId());
            }
            if (request.ownership().getDepartmentCode() != null) {
                this.departmentServicePort.getDepartment(request.ownership().getDepartmentCode());
            }
        }
    }

    private void validateUser(final UserId userId) {
        final User user = this.userService.findByUserId(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
    }
}
