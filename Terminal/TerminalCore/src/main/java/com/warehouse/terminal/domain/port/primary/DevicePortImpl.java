package com.warehouse.terminal.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.exception.DepartmentNotFoundException;
import com.warehouse.terminal.domain.exception.UserNotFoundException;
import com.warehouse.terminal.domain.model.Department;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DeviceSettings;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.command.*;
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.port.secondary.DepartmentServicePort;
import com.warehouse.terminal.domain.port.secondary.UserServicePort;
import com.warehouse.terminal.domain.service.DeviceGenericService;
import com.warehouse.terminal.domain.service.UserService;
import com.warehouse.terminal.domain.vo.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DevicePortImpl implements DevicePort {

    private final DeviceGenericService deviceGenericService;

    private final UserService userService;

    private final DepartmentServicePort departmentServicePort;

    private final UserServicePort userServicePort;

    public DevicePortImpl(final DeviceGenericService deviceGenericService,
                          final UserService userService,
                          final DepartmentServicePort departmentServicePort,
                          final UserServicePort userServicePort) {
        this.deviceGenericService = deviceGenericService;
        this.userService = userService;
        this.departmentServicePort = departmentServicePort;
        this.userServicePort = userServicePort;
    }

    @Override
    public DeviceCreateResult create(final DeviceCreateCommand command) {
        logDeviceCreation(command);
        final UserId userId = command.getUserId();
        final DeviceType deviceType = command.getDeviceType();
        final DepartmentCode departmentCode = command.getDepartmentCode();
        final User user = this.userServicePort.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        final Department department = this.departmentServicePort.getDepartment(departmentCode);
        if (department == null) {
            throw new DepartmentNotFoundException(departmentCode);
        }
        final DeviceId deviceId = this.deviceGenericService.nextDeviceId(deviceType);
        final DeviceIdentity deviceDeviceIdentity = command.getIdentity();
        final DeviceHardware deviceHardware = command.getHardware();
        final DeviceSoftware deviceSoftware = command.getSoftware();
        final DeviceNetwork deviceNetwork = command.getNetwork();
        final DeviceLocation deviceLocation = command.getLocation();
		final OwnershipProfile ownershipProfile = OwnershipProfile.initializeOwnership("", userId, departmentCode,
                null);
        
		final Device device = 
                getDevice(command, deviceType, deviceId, deviceDeviceIdentity, deviceHardware, deviceSoftware, deviceNetwork, deviceLocation, ownershipProfile);
        this.deviceGenericService.create(device);

        return new DeviceCreateResult(deviceId);
    }

	private Device getDevice(final DeviceCreateCommand command, final DeviceType deviceType, final DeviceId deviceId,
			final DeviceIdentity deviceDeviceIdentity, final DeviceHardware deviceHardware,
			final DeviceSoftware deviceSoftware, final DeviceNetwork deviceNetwork, final DeviceLocation deviceLocation,
			final OwnershipProfile ownershipProfile) {
		final Device device;
		if (deviceType.equals(DeviceType.TERMINAL)) {
			device = new Terminal(deviceId, deviceDeviceIdentity, deviceHardware, deviceSoftware,
					deviceNetwork, deviceLocation, ownershipProfile);
		} else if (deviceType.equals(DeviceType.SCANNER)) {
			device = new Scanner(deviceId, deviceDeviceIdentity, deviceHardware, deviceNetwork, ownershipProfile,
					command.getScanType(), command.getScannerType());
		} else {
			device = new Mobile(deviceId, deviceDeviceIdentity, deviceHardware, deviceSoftware,
					deviceNetwork, deviceLocation, ownershipProfile);
		}
		return device;
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

    @Override
    public void updateDeviceType(final DeviceTypeUpdateCommand request) {
        this.deviceGenericService.updateDeviceType(request.deviceId(), request.deviceType());
    }

    @Override
    public void updateActiveField(final DeviceActiveUpdateCommand request) {
        this.deviceGenericService.updateActive(request.deviceId(), request.active());
    }

    @Override
    public void updateStatusField(final DeviceStatusUpdateCommand request) {
        this.deviceGenericService.updateStatus(request.deviceId(), request.status());
    }

    @Override
    public void updateIdentityField(final DeviceIdentityUpdateCommand request) {
        this.deviceGenericService.updateIdentity(request.deviceId(), request.identity());
    }

    @Override
    public void updateHardwareField(final DeviceHardwareUpdateCommand request) {
        this.deviceGenericService.updateHardware(request.deviceId(), request.hardware());
    }

    @Override
    public void updateSoftwareField(final DeviceSoftwareUpdateCommand request) {
        this.deviceGenericService.updateSoftware(request.deviceId(), request.software());
    }

    @Override
    public void updateNetworkField(final DeviceNetworkUpdateCommand request) {
        this.deviceGenericService.updateNetwork(request.deviceId(), request.network());
    }

    @Override
    public void updateSecurityField(final DeviceSecurityUpdateCommand request) {
        this.deviceGenericService.updateSecurity(request.deviceId(), request.security());
    }

    @Override
    public void updateLocationField(final DeviceLocationUpdateCommand request) {
        this.deviceGenericService.updateLocation(request.deviceId(), request.location());
    }

    @Override
    public void updateOwnershipField(final DeviceOwnershipUpdateCommand request) {
        validateOwnership(request.ownership());
        this.deviceGenericService.updateOwnership(request.deviceId(), request.ownership());
    }

    @Override
    public void updateVersionField(final DeviceVersionUpdateCommand request) {
        this.deviceGenericService.updateVersionField(request.deviceId(), request.version());
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
        validateOwnership(request.ownership());
    }

    private void validateUser(final UserId userId) {
        final User user = this.userService.findByUserId(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
    }

    private void validateOwnership(final OwnershipProfile ownership) {
        if (ownership != null) {
            if (ownership.getUserId() != null) {
                validateUser(ownership.getUserId());
            }
            if (ownership.getDepartmentCode() != null) {
                this.departmentServicePort.getDepartment(ownership.getDepartmentCode());
            }
        }
    }

}
