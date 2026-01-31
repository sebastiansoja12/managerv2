package com.warehouse.terminal.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
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

    public DeviceServiceAdapter(final DevicePairService devicePairService,
                                final DeviceGenericService deviceGenericService,
                                final UserService userService) {
        this.devicePairService = devicePairService;
        this.deviceGenericService = deviceGenericService;
        this.userService = userService;
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

        return this.devicePairService.findByPairKey(pairKey)
                .filter(DevicePair::isPaired)
                .map(DevicePair::getDeviceId)
                .flatMap(this::resolveDeviceContext)
                .orElseGet(DeviceAuthenticationResponseDto::invalid);
    }

    private Optional<DeviceAuthenticationResponseDto> resolveDeviceContext(final DeviceId deviceId) {
        final Device device;
        try {
            device = this.deviceGenericService.findByDeviceId(deviceId);
        } catch (final RuntimeException ex) {
            return Optional.empty();
        }

        if (device == null || !DeviceStatus.ACTIVE.equals(device.getStatus())) {
            return Optional.empty();
        }

        final OwnershipProfile ownership = device.getOwnership();
        final DepartmentCode departmentCode = ownership != null ? ownership.getDepartmentCode() : null;
        final UserId userId = ownership != null ? ownership.getUserId() : null;
        final Username username = resolveUsername(userId);

        return Optional.of(new DeviceAuthenticationResponseDto(
                true,
                mapDeviceId(deviceId),
                mapDepartmentCode(departmentCode),
                mapUserId(userId),
                mapUsername(username)
        ));
    }

    private Username resolveUsername(final UserId userId) {
        if (userId == null) {
            return null;
        }
        try {
            final User user = this.userService.findByUserId(userId);
            return user != null ? user.username() : null;
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
}
