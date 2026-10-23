package com.warehouse.terminal.domain.port.primary;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.exception.DeviceAccessException;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DeviceActivationCode;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.command.DeviceActivationCommand;
import com.warehouse.terminal.domain.model.command.DeviceHeartbeatCommand;
import com.warehouse.terminal.domain.model.command.DeviceOperatorSessionCommand;
import com.warehouse.terminal.domain.model.command.DevicePairingCodeCommand;
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.service.*;
import com.warehouse.terminal.domain.vo.DeviceActivationCodeIssue;
import com.warehouse.terminal.domain.vo.DeviceActivationResult;
import com.warehouse.terminal.domain.vo.DeviceHardware;
import com.warehouse.terminal.domain.vo.DeviceHeartbeatResult;
import com.warehouse.terminal.domain.vo.DeviceIdentity;
import com.warehouse.terminal.domain.vo.DeviceLocation;
import com.warehouse.terminal.domain.vo.DeviceNetwork;
import com.warehouse.terminal.domain.vo.DeviceOperatorSessionResult;
import com.warehouse.terminal.domain.vo.DevicePairKey;
import com.warehouse.terminal.domain.vo.DeviceSoftware;
import com.warehouse.terminal.domain.vo.IssuedDevicePairKey;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DevicePairPortImpl implements DevicePairPort {

    private static final String INSTALLATION_ID_PREFIX = "app:";

    private final DeviceGenericService deviceGenericService;

    private final UserService userService;

    private final DevicePairService devicePairService;

    private final DeviceVersionService deviceVersionService;

    private final DevicePairValidationService devicePairValidationService;

    private final DeviceActivationService deviceActivationService;

    private final OperatorContext operatorContext;

    public DevicePairPortImpl(final DeviceGenericService deviceGenericService,
                              final UserService userService,
                              final DevicePairService devicePairService,
                              final DeviceVersionService deviceVersionService,
                              final DevicePairValidationService devicePairValidationService,
                              final DeviceActivationService deviceActivationService,
                              final OperatorContext operatorContext) {
        this.deviceGenericService = deviceGenericService;
        this.userService = userService;
        this.devicePairService = devicePairService;
        this.deviceVersionService = deviceVersionService;
        this.devicePairValidationService = devicePairValidationService;
        this.deviceActivationService = deviceActivationService;
        this.operatorContext = operatorContext;
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
    public void unpair(final Terminal terminal) {
        this.devicePairService.unpairDevice(terminal.getTerminalId());
    }

    @Transactional
    @Override
    public DeviceActivationCodeIssue issueActivationCode(final DeviceId deviceId) {
        final Device device = this.deviceGenericService.findByDeviceId(deviceId);
        if (!DeviceStatus.ACTIVE.equals(device.getStatus())) {
            throw DeviceAccessException.deviceNotActive();
        }
        final DeviceActivationCodeIssue issue = this.deviceActivationService.issue(deviceId);
        log.info("Issued activation code for device [{}]", deviceId.getValue());
        return issue;
    }

    @Transactional
    @Override
    public DeviceActivationResult activate(final DeviceActivationCommand command) {
        final DeviceActivationCode activationCode = this.deviceActivationService.consume(command.activationCode());
        final Device device = activationCode.isUserPairing()
                ? pairUserDevice(activationCode, command)
                : this.deviceGenericService.findByDeviceIdForAuthentication(activationCode.getDeviceId())
                        .filter(candidate -> DeviceStatus.ACTIVE.equals(candidate.getStatus()))
                        .orElseThrow(DeviceAccessException::deviceNotActive);
        final IssuedDevicePairKey pairKey = this.devicePairService.activateDevice(device);
        log.info("Activated device [{}]", device.getDeviceId().getValue());
        return new DeviceActivationResult(device, pairKey.pairKey(), pairKey.expiresAt());
    }

    @Transactional
    @Override
    public DeviceActivationCodeIssue issuePairingCode(final DevicePairingCodeCommand command) {
        final DeviceActivationCodeIssue issue = this.deviceActivationService.issueForUser(command);
        log.info("Issued device pairing code for user [{}]", command.userId().value());
        return issue;
    }

    private Device pairUserDevice(final DeviceActivationCode activationCode, final DeviceActivationCommand command) {
        if (StringUtils.isBlank(command.installationId())) {
            throw DeviceAccessException.missingInstallationId();
        }
        final String externalSystemId = INSTALLATION_ID_PREFIX + command.installationId().trim();
        return this.operatorContext.runAs(activationCode.getOperatorId(), activationCode.getUserId(),
                () -> this.deviceGenericService.findByExternalSystemId(externalSystemId)
                        .map(device -> assignToUser(device, activationCode.getUserId()))
                        .orElseGet(() -> createUserDevice(externalSystemId, activationCode, command)));
    }

    private Device assignToUser(final Device device, final UserId userId) {
        if (!DeviceStatus.ACTIVE.equals(device.getStatus())) {
            throw DeviceAccessException.deviceNotActive();
        }
        final boolean shared = device.getOwnership() != null && device.getOwnership().isShared();
        if (!shared && !userId.equals(device.getUserId())) {
            this.deviceGenericService.assignUser(device.getDeviceId(), userId);
            device.assignUser(userId);
            log.info("Assigned device [{}] to user [{}]", device.getDeviceId().getValue(), userId.value());
        }
        return device;
    }

    private Device createUserDevice(final String externalSystemId,
                                    final DeviceActivationCode activationCode,
                                    final DeviceActivationCommand command) {
        final DeviceType deviceType = command.deviceType() != null ? command.deviceType() : DeviceType.MOBILE;
        final DeviceId deviceId = this.deviceGenericService.nextDeviceId(deviceType);
        final DeviceIdentity identity = new DeviceIdentity();
        identity.setExternalSystemId(externalSystemId);
        final DeviceHardware hardware = new DeviceHardware();
        hardware.setManufacturer(StringUtils.trimToNull(command.manufacturer()));
        hardware.setModel(StringUtils.trimToNull(command.model()));
        final DeviceSoftware software = new DeviceSoftware();
        software.setOsName(StringUtils.trimToNull(command.osName()));
        software.setOsVersion(StringUtils.trimToNull(command.osVersion()));
        software.setAppVersion(StringUtils.trimToNull(command.appVersion()));
        final OwnershipProfile ownership = OwnershipProfile.initializeOwnership("", activationCode.getUserId(),
                activationCode.getDepartmentCode(), null, activationCode.getOperatorId());
        final Device device = switch (deviceType) {
            case TERMINAL -> new Terminal(deviceId, identity, hardware, software, new DeviceNetwork(),
                    new DeviceLocation(), ownership);
            case SCANNER -> new Scanner(deviceId, identity, hardware, new DeviceNetwork(), ownership, null, null);
            default -> new Mobile(deviceId, identity, hardware, software, new DeviceNetwork(), new DeviceLocation(),
                    ownership);
        };
        this.deviceGenericService.create(device);
        log.info("Created device [{}] while pairing with user [{}]", deviceId.getValue(),
                activationCode.getUserId().value());
        return device;
    }

    @Transactional
    @Override
    public void revoke(final DeviceId deviceId) {
        this.deviceGenericService.findByDeviceId(deviceId);
        this.deviceActivationService.discardUnused(deviceId);
        if (this.devicePairService.revokePairing(deviceId)) {
            log.info("Revoked pairing of device [{}]", deviceId.getValue());
        }
    }

    @Override
    public DeviceOperatorSessionResult startOperatorSession(final DeviceOperatorSessionCommand command) {
        final DevicePair devicePair = findValidPair(command.pairKey());
        final Device device = this.deviceGenericService.findByDeviceId(devicePair.getDeviceId());
        this.devicePairValidationService.validateOperator(device, command.userId());
        final DevicePair session = this.devicePairService.startOperatorSession(devicePair, command.userId());
        log.info("Started operator session on device [{}]", devicePair.getDeviceId().getValue());
        return new DeviceOperatorSessionResult(device, session.getOperatorSessionExpiresAt());
    }

    @Override
    public void endOperatorSession(final DeviceOperatorSessionCommand command) {
        final DevicePair devicePair = findValidPair(command.pairKey());
        this.devicePairService.endOperatorSession(devicePair, command.userId());
    }

    @Override
    public DeviceHeartbeatResult heartbeat(final DeviceHeartbeatCommand command) {
        final DevicePair devicePair = findValidPair(command.pairKey());
        final DeviceId deviceId = devicePair.getDeviceId();
        final Device device = this.deviceGenericService.findByDeviceId(deviceId);
        if (StringUtils.isNotBlank(command.version()) && !command.version().equals(device.getVersion())) {
            this.deviceGenericService.updateVersionField(deviceId, command.version());
        }
        reportHardware(device, command);
        reportSoftware(device, command);
        this.devicePairService.markSeen(devicePair);
        final boolean updateRequired = StringUtils.isNotBlank(command.version())
                && updateRequired(deviceId, new DeviceVersion(command.version(), deviceId));
        return new DeviceHeartbeatResult(updateRequired);
    }

    private void reportHardware(final Device device, final DeviceHeartbeatCommand command) {
        final String manufacturer = StringUtils.trimToNull(command.manufacturer());
        final String model = StringUtils.trimToNull(command.model());
        final DeviceHardware current = device.getHardware();
        if ((manufacturer == null && model == null) || (current != null
                && Objects.equals(manufacturer, current.getManufacturer()) && Objects.equals(model, current.getModel()))) {
            return;
        }
        final DeviceHardware hardware = new DeviceHardware();
        hardware.setManufacturer(manufacturer);
        hardware.setModel(model);
        this.deviceGenericService.updateHardware(device.getDeviceId(), hardware);
    }

    private void reportSoftware(final Device device, final DeviceHeartbeatCommand command) {
        if (DeviceType.SCANNER.equals(device.getDeviceType())
                || StringUtils.isAllBlank(command.osName(), command.osVersion())) {
            return;
        }
        final String osName = StringUtils.trimToNull(command.osName());
        final String osVersion = StringUtils.trimToNull(command.osVersion());
        final String appVersion = StringUtils.isNotBlank(command.version()) ? command.version() : device.getVersion();
        final DeviceSoftware current = device.getSoftware();
        if (current != null && Objects.equals(osName, current.getOsName())
                && Objects.equals(osVersion, current.getOsVersion())
                && Objects.equals(appVersion, current.getAppVersion())) {
            return;
        }
        final DeviceSoftware software = new DeviceSoftware();
        software.setOsName(osName);
        software.setOsVersion(osVersion);
        software.setAppVersion(appVersion);
        this.deviceGenericService.updateSoftware(device.getDeviceId(), software);
    }

    private DevicePair findValidPair(final DevicePairKey pairKey) {
        if (pairKey == null || StringUtils.isBlank(pairKey.value())) {
            throw DeviceAccessException.invalidCredentials();
        }
        return this.devicePairService.findValidByPairKey(pairKey.value())
                .orElseThrow(DeviceAccessException::invalidCredentials);
    }
}
