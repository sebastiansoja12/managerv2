package com.warehouse.terminal.infrastructure.adapter.primary.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.warehouse.terminal.infrastructure.adapter.primary.exception.DeviceInvalidCreateRequestException;
import com.warehouse.terminal.infrastructure.adapter.primary.exception.DeviceInvalidUpdateRequestException;
import com.warehouse.terminal.request.DeviceCreateRequestDto;
import com.warehouse.terminal.request.DeviceHardwareRequestDto;
import com.warehouse.terminal.request.DeviceIdentityRequestDto;
import com.warehouse.terminal.request.DeviceLocationRequestDto;
import com.warehouse.terminal.request.DeviceNetworkRequestDto;
import com.warehouse.terminal.request.DeviceOwnershipRequestDto;
import com.warehouse.terminal.request.DeviceSecurityRequestDto;
import com.warehouse.terminal.request.DeviceSoftwareRequestDto;
import com.warehouse.terminal.request.DeviceUpdateRequestDto;
import com.warehouse.terminal.dto.DeviceTypeDto;

@Service
public class DeviceRequestValidationService {

    public void validateCreateRequest(final DeviceCreateRequestDto request) {
        final List<DeviceInvalidParam> validationErrors = new ArrayList<>();

        if (request == null) {
            validationErrors.add(DeviceInvalidParam.missingRequestPayload());
        } else {
            if (request.userId() == null || request.userId().value() == null) {
                validationErrors.add(DeviceInvalidParam.missingUserId());
            }
            if (request.departmentCode() == null || StringUtils.isBlank(request.departmentCode().value())) {
                validationErrors.add(DeviceInvalidParam.missingDepartmentCode());
            }
            if (request.deviceType() == null) {
                validationErrors.add(DeviceInvalidParam.missingDeviceType());
            }
            if (request.version() != null && StringUtils.isBlank(request.version().value())) {
                validationErrors.add(DeviceInvalidParam.blankVersion());
            }
            validateScannerSpecificFieldsOnCreate(request, validationErrors);

            validateIdentity(request.identity(), validationErrors);
            validateHardware(request.hardware(), validationErrors);
            validateSoftware(request.software(), validationErrors);
            validateNetwork(request.network(), validationErrors);
            validateSecurity(request.security(), validationErrors);
            validateLocation(request.location(), validationErrors);
        }

        throwIfCreateValidationFailed(validationErrors);
    }

    public void validateUpdateRequest(final DeviceUpdateRequestDto request) {
        final List<DeviceInvalidParam> validationErrors = new ArrayList<>();

        if (request == null) {
            validationErrors.add(DeviceInvalidParam.missingRequestPayload());
            throwIfUpdateValidationFailed(validationErrors);
        } else {
            if (request.deviceId() == null || request.deviceId().value() == null) {
                validationErrors.add(DeviceInvalidParam.missingDeviceId());
            }
            if (!hasAnyUpdatableField(request)) {
                validationErrors.add(DeviceInvalidParam.missingUpdatableFields());
            }
            if (request.externalDeviceId() != null && StringUtils.isBlank(request.externalDeviceId())) {
                validationErrors.add(DeviceInvalidParam.blankExternalDeviceId());
            }
            if (request.status() != null && !isAllowedStatus(request.status())) {
                validationErrors.add(DeviceInvalidParam.invalidStatus());
            }
            if (request.version() != null && StringUtils.isBlank(request.version().value())) {
                validationErrors.add(DeviceInvalidParam.blankVersion());
            }

            if (request.identity() != null) {
                validateIdentity(request.identity(), validationErrors);
            }
            if (request.hardware() != null) {
                validateHardware(request.hardware(), validationErrors);
            }
            if (request.software() != null) {
                validateSoftwareForUpdate(request.software(), validationErrors);
            }
            if (request.network() != null) {
                validateNetwork(request.network(), validationErrors);
            }
            if (request.security() != null) {
                validateSecurity(request.security(), validationErrors);
            }
            if (request.location() != null) {
                validateLocation(request.location(), validationErrors);
            }
            if (request.ownership() != null) {
                validateOwnership(request.ownership(), validationErrors);
            }
        }

        throwIfUpdateValidationFailed(validationErrors);
    }

    private void validateIdentity(final DeviceIdentityRequestDto identity,
                                  final List<DeviceInvalidParam> validationErrors) {
        if (identity == null) {
            validationErrors.add(DeviceInvalidParam.identityRequired());
        } else {
            if (isIdentityMissing(identity)) {
                validationErrors.add(DeviceInvalidParam.invalidIdentityIdentifiers());
            }
        }
    }

    private void validateHardware(final DeviceHardwareRequestDto hardware,
                                  final List<DeviceInvalidParam> validationErrors) {
        if (hardware == null) {
            validationErrors.add(DeviceInvalidParam.hardwareRequired());
        } else {
            if (StringUtils.isBlank(hardware.manufacturer())
                    && StringUtils.isBlank(hardware.model())
                    && StringUtils.isBlank(hardware.productName())) {
                validationErrors.add(DeviceInvalidParam.invalidHardware());
            }
        }
    }

    private void validateSoftware(final DeviceSoftwareRequestDto software,
                                  final List<DeviceInvalidParam> validationErrors) {
        if (software == null) {
            validationErrors.add(DeviceInvalidParam.softwareRequired());
        }
    }

    private void validateSoftwareForUpdate(final DeviceSoftwareRequestDto software,
                                           final List<DeviceInvalidParam> validationErrors) {
        if (StringUtils.isBlank(software.appVersion())
                && StringUtils.isBlank(software.osName())
                && StringUtils.isBlank(software.osVersion())
                && StringUtils.isBlank(software.firmwareVersion())
                && StringUtils.isBlank(software.kernelVersion())
                && StringUtils.isBlank(software.bootloaderVersion())
                && StringUtils.isBlank(software.buildNumber())
                && software.rooted() == null
                && software.developerModeEnabled() == null
                && (software.installedApps() == null || software.installedApps().isEmpty())) {
            validationErrors.add(DeviceInvalidParam.softwareUpdateEmpty());
        }
    }

    private void validateNetwork(final DeviceNetworkRequestDto network,
                                 final List<DeviceInvalidParam> validationErrors) {
        if (network == null) {
            validationErrors.add(DeviceInvalidParam.networkRequired());
        } else {
            if (StringUtils.isNotBlank(network.networkType())
                    && !isAllowedNetworkType(network.networkType())) {
                validationErrors.add(DeviceInvalidParam.invalidNetworkType());
            }
        }
    }

    private void validateSecurity(final DeviceSecurityRequestDto security,
                                  final List<DeviceInvalidParam> validationErrors) {
        if (security == null) {
            validationErrors.add(DeviceInvalidParam.securityRequired());
        } else {
            if (security.failedLoginAttempts() != null && security.failedLoginAttempts() < 0) {
                validationErrors.add(DeviceInvalidParam.invalidFailedLoginAttempts());
            }
        }
    }

    private void validateLocation(final DeviceLocationRequestDto location,
                                  final List<DeviceInvalidParam> validationErrors) {
        if (location == null) {
            validationErrors.add(DeviceInvalidParam.locationRequired());
        } else {
            final boolean hasLatitude = location.latitude() != null;
            final boolean hasLongitude = location.longitude() != null;
            if (hasLatitude != hasLongitude) {
                validationErrors.add(DeviceInvalidParam.invalidLocationCoordinates());
            }
        }
    }

    private void validateOwnership(final DeviceOwnershipRequestDto ownership,
                                   final List<DeviceInvalidParam> validationErrors) {
        if (!hasAnyOwnershipField(ownership)) {
            validationErrors.add(DeviceInvalidParam.ownershipUpdateEmpty());
        }

        if (ownership.userId() != null && ownership.userId().value() == null) {
            validationErrors.add(DeviceInvalidParam.ownershipUserIdMissingValue());
        }

        if (ownership.previousUserId() != null && ownership.previousUserId().value() == null) {
            validationErrors.add(DeviceInvalidParam.ownershipPreviousUserIdMissingValue());
        }

        if (ownership.departmentCode() != null && StringUtils.isBlank(ownership.departmentCode().value())) {
            validationErrors.add(DeviceInvalidParam.ownershipBlankDepartmentCode());
        }
    }

    private void throwIfCreateValidationFailed(final List<DeviceInvalidParam> validationErrors) {
        if (!validationErrors.isEmpty()) {
            throw DeviceInvalidCreateRequestException.of(validationErrors);
        }
    }

    private void throwIfUpdateValidationFailed(final List<DeviceInvalidParam> validationErrors) {
        if (!validationErrors.isEmpty()) {
            throw DeviceInvalidUpdateRequestException.of(validationErrors);
        }
    }

    private void validateScannerSpecificFieldsOnCreate(final DeviceCreateRequestDto request,
                                                       final List<DeviceInvalidParam> validationErrors) {
        if (request.deviceType() == null) {
            return;
        }

        if (!DeviceTypeDto.SCANNER.equals(request.deviceType())) {
            if (request.scanType() != null) {
                validationErrors.add(DeviceInvalidParam.scanTypeAllowedOnlyForScanner());
            }
            if (request.scannerType() != null) {
                validationErrors.add(DeviceInvalidParam.scannerTypeAllowedOnlyForScanner());
            }
        }
    }

    private boolean isIdentityMissing(final DeviceIdentityRequestDto identity) {
        return StringUtils.isBlank(identity.hardwareUuid())
                && StringUtils.isBlank(identity.serialNumber())
                && StringUtils.isBlank(identity.imei())
                && StringUtils.isBlank(identity.macAddress())
                && StringUtils.isBlank(identity.assetTag())
                && StringUtils.isBlank(identity.barcode())
                && StringUtils.isBlank(identity.externalSystemId())
                && StringUtils.isBlank(identity.mdmDeviceId());
    }

    private boolean isAllowedNetworkType(final String networkType) {
        final String value = networkType.toUpperCase();
        return "WIFI".equals(value)
                || "CELLULAR".equals(value)
                || "ETHERNET".equals(value)
                || "OFFLINE".equals(value)
                || "UNKNOWN".equals(value);
    }

    private boolean isAllowedStatus(final String status) {
        final String value = status.toUpperCase();
        return "ACTIVE".equals(value)
                || "BLOCKED".equals(value)
                || "LOST".equals(value)
                || "RETIRED".equals(value);
    }

    private boolean hasAnyUpdatableField(final DeviceUpdateRequestDto request) {
        return request.externalDeviceId() != null
                || request.deviceType() != null
                || request.status() != null
                || request.userId() != null
                || request.departmentCode() != null
                || request.version() != null
                || request.createdAt() != null
                || request.updatedAt() != null
                || request.lastUpdate() != null
                || request.active() != null
                || request.identity() != null
                || request.hardware() != null
                || request.software() != null
                || request.network() != null
                || request.security() != null
                || request.location() != null
                || request.ownership() != null;
    }

    private boolean hasAnyOwnershipField(final DeviceOwnershipRequestDto ownership) {
        return ownership.userId() != null
                || ownership.previousUserId() != null
                || ownership.departmentCode() != null
                || ownership.teamId() != null
                || ownership.vehicleId() != null
                || ownership.assignedRole() != null
                || ownership.assignedAt() != null
                || ownership.unassignedAt() != null;
    }
}
