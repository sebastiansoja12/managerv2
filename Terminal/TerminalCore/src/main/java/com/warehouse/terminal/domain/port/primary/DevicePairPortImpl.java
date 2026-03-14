package com.warehouse.terminal.domain.port.primary;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.enumeration.PairStatus;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.model.command.DevicePairRequest;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.service.*;
import com.warehouse.terminal.domain.vo.DevicePairResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DevicePairPortImpl implements DevicePairPort {

    private final DeviceGenericService deviceGenericService;

    private final UserService userService;

    private final DevicePairService devicePairService;

    private final DeviceVersionService deviceVersionService;

    private final DevicePairValidationService devicePairValidationService;

    public DevicePairPortImpl(final DeviceGenericService deviceGenericService,
                              final UserService userService,
                              final DevicePairService devicePairService,
                              final DeviceVersionService deviceVersionService,
                              final DevicePairValidationService devicePairValidationService) {
        this.deviceGenericService = deviceGenericService;
        this.userService = userService;
        this.devicePairService = devicePairService;
        this.deviceVersionService = deviceVersionService;
        this.devicePairValidationService = devicePairValidationService;
    }

    @Override
    public boolean isPaired(final DeviceId deviceId) {
        final DevicePair devicePair = this.devicePairService.findByDeviceId(deviceId);
        return devicePair != null && devicePair.isPaired();
    }

    @Override
    public boolean isValid(final DeviceId deviceId) {
        final Device device = this.deviceGenericService.findByDeviceId(deviceId);
        final DeviceVersion deviceVersion = this.deviceVersionService.findByDeviceId(deviceId);
        final DevicePair devicePair = this.devicePairService.findByDeviceId(deviceId);
        return device != null && deviceVersion != null && devicePair != null;
    }

    @Override
    public boolean isPairKeyValid(final String pairKey) {
        if (StringUtils.isBlank(pairKey)) {
            return false;
        }
        return this.devicePairService.findValidByPairKey(pairKey)
                .map(DevicePair::getDeviceId)
                .map(deviceId -> {
                    try {
                        return this.deviceGenericService.findByDeviceId(deviceId);
                    } catch (final RuntimeException ex) {
                        return null;
                    }
                })
                .map(device -> DeviceStatus.ACTIVE.equals(device.getStatus()))
                .orElse(false);
    }

    @Override
    public boolean isUserValid(final DeviceId deviceId, final Username username) {
        final Device device = this.deviceGenericService.findByDeviceId(deviceId);
        if (device == null) {
            return false;
        }
        return this.userService.existsByUsername(username);
    }

    @Override
    public boolean isVersionValid(final DeviceId deviceId, final DeviceVersion deviceVersion) {
        final Device device = this.deviceGenericService.findByDeviceId(deviceId);
        if (device == null || deviceVersion == null) {
            return false;
        }
        if (device instanceof Terminal terminal) {
            return terminal.getVersion() != null && terminal.getVersion().equals(deviceVersion.getVersion());
        }
        return false;
    }

    @Override
    public boolean updateRequired(final DeviceId deviceId, final DeviceVersion version) {
        final DeviceVersion deviceVersion = this.deviceVersionService.findByDeviceId(deviceId);
        return deviceVersion != null && !deviceVersion.getVersion().equals(version.getVersion());
    }

    @Override
    public DevicePairResponse pair(final DevicePairRequest request) {
        final Device device = this.devicePairValidationService.validate(request);
        final DevicePair devicePair = this.devicePairService.pairDevice(device);
        log.info("Paired device [{}]", devicePair.getDeviceId().getValue());
        return new DevicePairResponse(request.getUserId(), devicePair.getDeviceId(), devicePair.getDevicePairId(),
                devicePair.isPaired() ? PairStatus.PAIRED.name() : PairStatus.UNPAIRED.name(),
                devicePair.getPairKey(), true, true, true);
    }

    @Override
    public void unpair(final Terminal terminal) {
        this.devicePairService.unpairDevice(terminal.getTerminalId());
    }
}
