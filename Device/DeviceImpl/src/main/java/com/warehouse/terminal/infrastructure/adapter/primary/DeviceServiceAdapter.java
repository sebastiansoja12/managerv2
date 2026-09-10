package com.warehouse.terminal.infrastructure.adapter.primary;

import java.util.List;
import com.warehouse.terminal.dto.DeviceAccessDto;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.service.DeviceGenericService;
import com.warehouse.terminal.domain.service.DevicePairService;
import com.warehouse.terminal.domain.service.UserService;
import com.warehouse.terminal.dto.DepartmentCodeDto;
import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.UserIdDto;
import com.warehouse.terminal.dto.UsernameDto;
import com.warehouse.terminal.response.DeviceAuthenticationResponseDto;
import com.warehouse.terminal.response.DeviceInformationResponseDto;
import com.warehouse.terminal.domain.vo.User;

import java.util.Optional;

public class DeviceServiceAdapter implements DeviceApiService {

    private final DevicePairService devicePairService;
    private final DeviceGenericService deviceGenericService;
    private final UserService userService;
    private final OperatorContext operatorContext;

    public DeviceServiceAdapter(final DevicePairService devicePairService,
                                final DeviceGenericService deviceGenericService,
                                final UserService userService,
                                final OperatorContext operatorContext) {
        this.devicePairService = devicePairService;
        this.deviceGenericService = deviceGenericService;
        this.userService = userService;
        this.operatorContext = operatorContext;
    }

    @Override
    public DeviceInformationResponseDto isDeviceValidByPairKey(final String pairKey) {
        final DeviceAuthenticationResponseDto authentication = authenticateByPairKey(pairKey);
        return new DeviceInformationResponseDto(Boolean.TRUE.equals(authentication.value()));
    }

    @Override
    public DeviceAuthenticationResponseDto authenticateByPairKey(final String pairKey) {
        if (pairKey == null || pairKey.isBlank()) {
            return DeviceAuthenticationResponseDto.invalid();
        }

        return this.devicePairService.findValidByPairKey(pairKey)
                .map(DevicePair::getDeviceId)
                .flatMap(this::resolveDeviceContext)
                .orElseGet(DeviceAuthenticationResponseDto::invalid);
    }

    private Optional<DeviceAuthenticationResponseDto> resolveDeviceContext(final DeviceId deviceId) {
        return this.deviceGenericService.findByDeviceIdForAuthentication(deviceId)
                .filter(device -> DeviceStatus.ACTIVE.equals(device.getStatus()))
                .map(device -> toAuthenticationResponse(deviceId, device.getOwnership()));
    }

    private DeviceAuthenticationResponseDto toAuthenticationResponse(final DeviceId deviceId,
                                                                     final OwnershipProfile ownership) {
        final DepartmentCode departmentCode = ownership != null ? ownership.getDepartmentCode() : null;
        final UserId userId = ownership != null ? ownership.getUserId() : null;
        final OperatorId operatorId = ownership != null ? ownership.getOperatorId() : null;
        final User user = resolveUser(operatorId, userId);
        final Username username = user != null ? user.username() : null;

        return new DeviceAuthenticationResponseDto(
                true,
                mapDeviceId(deviceId),
                mapDepartmentCode(departmentCode),
                mapUserId(userId),
                mapUsername(username),
                operatorId
        );
    }

    private User resolveUser(final OperatorId operatorId, final UserId userId) {
        if (operatorId == null || userId == null) {
            return null;
        }
        try {
            return this.operatorContext.runAs(operatorId, () -> this.userService.findByUserId(userId));
        } catch (final RuntimeException ex) {
            return null;
        }
    }

    private DeviceIdDto mapDeviceId(final DeviceId deviceId) {
        return deviceId != null ? new DeviceIdDto(deviceId.value()) : null;
    }

    private DepartmentCodeDto mapDepartmentCode(final DepartmentCode departmentCode) {
        return departmentCode != null ? new DepartmentCodeDto(departmentCode.value()) : null;
    }

    private UserIdDto mapUserId(final UserId userId) {
        return userId != null ? new UserIdDto(userId.value()) : null;
    }

    private UsernameDto mapUsername(final Username username) {
        return username != null ? new UsernameDto(username.value()) : null;
    }

    @Override
    public List<DeviceAccessDto> findAllAccess() {
        return deviceGenericService.findAll()
                .stream()
                .map(this::toAccess)
                .toList();
    }

    @Override
    public boolean revokeAccess(final DeviceIdDto deviceId) {
        return devicePairService.revokePairing(new DeviceId(deviceId.value()));
    }

    @Override
    public void changeStatus(final DeviceIdDto deviceId, final String status) {
        deviceGenericService.updateStatus(new DeviceId(deviceId.value()), DeviceStatus.valueOf(status));
    }

    @Override
    public int retireAll() {
        final List<Device> devices = deviceGenericService.findAll();
        devices.forEach(device -> {
            devicePairService.revokePairing(device.getDeviceId());
            deviceGenericService.updateStatus(device.getDeviceId(), DeviceStatus.RETIRED);
        });
        return devices.size();
    }

    private DeviceAccessDto toAccess(final Device device) {
        final Optional<DevicePair> devicePair = devicePairService.findExistingByDeviceId(device.getDeviceId());
        return new DeviceAccessDto(
                device.getDeviceId() != null ? device.getDeviceId().value() : null,
                device.getDeviceType() != null ? device.getDeviceType().name() : null,
                device.getStatus() != null ? device.getStatus().name() : null,
                device.getUserId() != null ? device.getUserId().value() : null,
                device.getDepartmentCode() != null ? device.getDepartmentCode().getValue() : null,
                versionOf(device),
                lastUpdateOf(device),
                devicePair.map(DevicePair::isPaired).orElse(false),
                devicePair.map(DevicePair::getPairKeyExpiresAt).orElse(null),
                devicePair.map(DevicePair::getLoginTime).orElse(null));
    }

    private String versionOf(final Device device) {
        try {
            return device.getVersion();
        } catch (UnsupportedOperationException exception) {
            return null;
        }
    }

    private java.time.Instant lastUpdateOf(final Device device) {
        try {
            return device.getLastUpdate();
        } catch (UnsupportedOperationException exception) {
            return null;
        }
    }
}
