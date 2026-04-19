package com.warehouse.terminal.infrastructure.adapter.primary.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.DeviceUserType;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.enumeration.NetworkType;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.model.command.DeviceCreateCommand;
import com.warehouse.terminal.domain.model.command.DevicePairRequest;
import com.warehouse.terminal.domain.model.command.DeviceUpdateCommand;
import com.warehouse.terminal.domain.vo.*;
import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.TerminalIdDto;
import com.warehouse.terminal.request.*;

@Mapper
public interface TerminalRequestMapper {

    default DevicePairRequest map(final DevicePairRequestDto terminalPairRequest) {
        final UserId userId = new UserId(terminalPairRequest.userId().value());
        final DeviceId deviceId = new DeviceId(terminalPairRequest.terminalId().value());
        return new DevicePairRequest(deviceId, userId);
    }

    default TerminalIdDto map(final DeviceId deviceId) {
        return new TerminalIdDto(deviceId != null ? deviceId.value() : null);
    }

    default DeviceCreateCommand map(final DeviceCreateRequestDto request) {
        return DeviceCreateCommand.builder()
                .userId(new UserId(request.userId().value()))
                .departmentCode(new DepartmentCode(request.departmentCode().value()))
                .supplierCode(request.supplierCode() != null ? new SupplierCode(request.supplierCode().value()) : null)
                .version(request.version() != null ? request.version().value() : null)
                .deviceUserType(request.deviceUserType() != null ? DeviceUserType.valueOf(request.deviceUserType().name()) : null)
                .deviceType(request.deviceType() != null ? DeviceType.valueOf(request.deviceType().name()) : null)
                .scanType(request.scanType() != null ? Scanner.ScanType.valueOf(request.scanType().name()) : null)
                .scannerType(request.scannerType() != null ? Scanner.ScannerType.valueOf(request.scannerType().name()) : null)
                .identity(map(request.identity()))
                .hardware(map(request.hardware()))
                .software(map(request.software()))
                .network(map(request.network()))
                .security(map(request.security()))
                .location(map(request.location()))
                .build();
    }

    default DeviceUpdateCommand map(final DeviceUpdateRequestDto request) {
        return new DeviceUpdateCommand(
                map(request.deviceId()),
                mapExternalId(request.externalDeviceId()),
                request.deviceType() != null ? DeviceType.valueOf(request.deviceType().name()) : null,
                mapStatus(request.status()),
                request.userId() != null ? new UserId(request.userId().value()) : null,
                request.departmentCode() != null ? new DepartmentCode(request.departmentCode().value()) : null,
                request.version() != null ? request.version().value() : null,
                request.createdAt(),
                request.updatedAt(),
                request.lastUpdate(),
                request.active(),
                map(request.identity()),
                map(request.hardware()),
                map(request.software()),
                map(request.network()),
                map(request.security()),
                map(request.location()),
                map(request.ownership())
        );
    }

    DeviceTypeChangeCommand map(final DeviceTypeRequestDto deviceTypeRequest);

    default DeviceId map(final DeviceIdDto deviceIdDto) {
        return new DeviceId(deviceIdDto.value());
    }

    DeviceUserRequest map(final DeviceUserRequestDto deviceUserRequest);

    @Mapping(target = "version", source = "version.value")
    DeviceVersionRequest map(final DeviceVersionRequestDto deviceVersionRequest);

    default IdentityInfo map(final DeviceIdentityRequestDto request) {
        if (request == null) {
            return null;
        }
        UUID hardwareUuid = null;
        if (request.hardwareUuid() != null && !request.hardwareUuid().isBlank()) {
            hardwareUuid = UUID.fromString(request.hardwareUuid());
        }
        return new IdentityInfo(
                request.assetTag(),
                request.barcode(),
                request.externalSystemId(),
                hardwareUuid,
                request.imei(),
                request.macAddress(),
                request.mdmDeviceId(),
                request.serialNumber()
        );
    }

    default HardwareProfile map(final DeviceHardwareRequestDto request) {
        if (request == null) {
            return null;
        }
        return new HardwareProfile(
                request.cpu(),
                request.hasCamera(),
                request.hasGps(),
                request.hasNfc(),
                request.hasScanner(),
                request.manufacturer(),
                request.model(),
                request.productName(),
                request.ramMb(),
                request.ruggedized(),
                request.screenResolution(),
                request.storageMb()
        );
    }

    default SoftwareProfile map(final DeviceSoftwareRequestDto request) {
        if (request == null) {
            return null;
        }
        return new SoftwareProfile(
                request.appVersion(),
                request.bootloaderVersion(),
                request.buildNumber(),
                request.developerModeEnabled(),
                request.firmwareVersion(),
                request.installedApps(),
                request.kernelVersion(),
                request.osName(),
                request.osVersion(),
                request.rooted()
        );
    }

    default NetworkProfile map(final DeviceNetworkRequestDto request) {
        if (request == null) {
            return null;
        }
        final NetworkType networkType;
        if (request.networkType() == null) {
            networkType = NetworkType.UNKNOWN;
        } else {
            try {
                networkType = NetworkType.valueOf(request.networkType().toUpperCase());
            } catch (final IllegalArgumentException ex) {
                return new NetworkProfile(
                        request.bluetoothMac(),
                        request.carrier(),
                        request.ipAddress(),
                        NetworkType.UNKNOWN,
                        request.publicIpAddress(),
                        request.roaming(),
                        request.simSerial(),
                        request.vpnConnected(),
                        request.wifiSsid()
                );
            }
        }
        return new NetworkProfile(
                request.bluetoothMac(),
                request.carrier(),
                request.ipAddress(),
                networkType,
                request.publicIpAddress(),
                request.roaming(),
                request.simSerial(),
                request.vpnConnected(),
                request.wifiSsid()
        );
    }

    default SecurityProfile map(final DeviceSecurityRequestDto request) {
        if (request == null) {
            return null;
        }
        return new SecurityProfile(
                request.biometricEnabled(),
                request.certificateFingerprint(),
                request.compromised(),
                request.encrypted(),
                request.failedLoginAttempts(),
                request.lastSecurityScanAt(),
                request.screenLockEnabled(),
                request.secureBootEnabled(),
                request.securityPolicyVersion(),
                request.tamperDetected()
        );
    }

    default LocationProfile map(final DeviceLocationRequestDto request) {
        if (request == null) {
            return null;
        }
        return new LocationProfile(
                request.latitude(),
                request.longitude(),
                request.altitude(),
                request.accuracyMeters(),
                request.lastKnownAddress(),
                request.geoZone(),
                request.gpsEnabled(),
                request.lastLocationUpdateAt()
        );
    }

    default OwnershipProfile map(final DeviceOwnershipRequestDto request) {
        if (request == null) {
            return null;
        }
        return new OwnershipProfile(
                request.assignedAt(),
                request.assignedRole(),
                request.departmentCode() != null ? new DepartmentCode(request.departmentCode().value()) : null,
                request.previousUserId() != null ? new UserId(request.previousUserId().value()) : null,
                request.teamId() != null ? new TeamId(request.teamId()) : null,
                request.unassignedAt(),
                request.userId() != null ? new UserId(request.userId().value()) : null,
                request.vehicleId() != null ? new VehicleId(request.vehicleId()) : null
        );
    }

    default ExternalId<String> mapExternalId(final String externalDeviceId) {
        if (externalDeviceId == null || externalDeviceId.isBlank()) {
            return null;
        }
        return new ExternalId<>(externalDeviceId);
    }

    default DeviceStatus mapStatus(final String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        return DeviceStatus.valueOf(status.toUpperCase());
    }
}
