package com.warehouse.terminal.infrastructure.adapter.primary;

import java.time.Instant;
import java.util.List;
import com.warehouse.terminal.dto.DeviceAccessDto;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.terminal.domain.enumeration.DeviceAssignmentMode;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.port.secondary.DepartmentRepository;
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
    private final DepartmentRepository departmentRepository;
    private final OperatorContext operatorContext;

    public DeviceServiceAdapter(final DevicePairService devicePairService,
                                final DeviceGenericService deviceGenericService,
                                final UserService userService,
                                final DepartmentRepository departmentRepository,
                                final OperatorContext operatorContext) {
        this.devicePairService = devicePairService;
        this.deviceGenericService = deviceGenericService;
        this.userService = userService;
        this.departmentRepository = departmentRepository;
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
                .flatMap(this::resolveDeviceContext)
                .orElseGet(DeviceAuthenticationResponseDto::invalid);
    }

    private Optional<DeviceAuthenticationResponseDto> resolveDeviceContext(final DevicePair devicePair) {
        return this.deviceGenericService.findByDeviceIdForAuthentication(devicePair.getDeviceId())
                .filter(device -> DeviceStatus.ACTIVE.equals(device.getStatus()))
                .map(device -> toAuthenticationResponse(devicePair.getDeviceId(), device.getOwnership(),
                        resolveOperatorUserId(devicePair, device.getOwnership())));
    }

    private UserId resolveOperatorUserId(final DevicePair devicePair, final OwnershipProfile ownership) {
        return devicePair.activeOperator(Instant.now())
                .orElseGet(() -> ownership != null && !ownership.isShared() ? ownership.getUserId() : null);
    }

    private DeviceAuthenticationResponseDto toAuthenticationResponse(final DeviceId deviceId,
                                                                     final OwnershipProfile ownership,
                                                                     final UserId userId) {
        final DepartmentCode departmentCode = ownership != null ? ownership.getDepartmentCode() : null;
        final OperatorId operatorId = ownership != null ? ownership.getOperatorId() : null;
        final User user = resolveUser(operatorId, userId);
        final Username username = user != null ? user.username() : null;

        return new DeviceAuthenticationResponseDto(
                true,
                mapDeviceId(deviceId),
                mapDepartmentCode(departmentCode),
                mapUserId(userId),
                mapUsername(username),
                operatorId,
                resolveDepartmentId(operatorId, departmentCode)
        );
    }

    private DepartmentId resolveDepartmentId(final OperatorId operatorId, final DepartmentCode departmentCode) {
        if (operatorId == null || departmentCode == null) {
            return null;
        }
        try {
            return this.operatorContext.runAs(operatorId,
                    () -> this.departmentRepository.findByDepartmentCode(departmentCode).getDepartmentId());
        } catch (final RuntimeException ex) {
            return null;
        }
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
        final Optional<UserId> activeOperator = devicePair.flatMap(pair -> pair.activeOperator(Instant.now()));
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
                devicePair.map(DevicePair::getLoginTime).orElse(null),
                assignmentModeOf(device),
                devicePair.map(DevicePair::getLastSeenAt).orElse(null),
                activeOperator.map(UserId::value).orElse(null),
                activeOperator.isPresent()
                        ? devicePair.map(DevicePair::getOperatorSessionExpiresAt).orElse(null)
                        : null);
    }

    private String assignmentModeOf(final Device device) {
        final OwnershipProfile ownership = device.getOwnership();
        return ownership != null && ownership.isShared()
                ? DeviceAssignmentMode.SHARED.name()
                : DeviceAssignmentMode.PERSONAL.name();
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
