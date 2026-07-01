package com.warehouse.terminal.infrastructure.adapter.secondary.mapper;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.vo.*;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.*;

@Component
public class EntityToModelMapper {

    public Terminal map(final TerminalEntity entity) {
        return new Terminal(
                entity.getId(),
                entity.getExternalDeviceId() != null ? entity.getExternalDeviceId() : ExternalId.generateId(),
                entity.getDeviceType() != null ? entity.getDeviceType() : DeviceType.TERMINAL,
                resolveStatus(entity.getStatus(), entity.getActive()),
                mapIdentity(entity.getIdentity()),
                mapHardware(entity.getHardware()),
                mapSoftware(entity.getSoftware(), null),
                mapNetwork(entity.getNetwork()),
                mapSecurity(entity.getSecurity()),
                mapLocation(entity.getLocation()),
                mapOwnership(entity.getUserId(), entity.getDepartmentCode(), entity.getOwnership()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                coalesce(entity.getVersion(), appVersion(entity.getSoftware())),
                entity.getLastUpdate());
    }

    public Mobile map(final MobileEntity entity) {
        return new Mobile(
                entity.getId(),
                entity.getExternalDeviceId() != null ? entity.getExternalDeviceId() : ExternalId.generateId(),
                entity.getDeviceType() != null ? entity.getDeviceType() : DeviceType.MOBILE,
                resolveStatus(entity.getStatus(), entity.getActive()),
                mapIdentity(entity.getIdentity()),
                mapHardware(entity.getHardware()),
                mapSoftware(entity.getSoftware(), entity.getInstalledApps()),
                mapNetwork(entity.getNetwork()),
                mapSecurity(entity.getSecurity()),
                mapLocation(entity.getLocation()),
                mapOwnership(entity.getUserId(), entity.getDepartmentCode(), entity.getOwnership()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                coalesce(entity.getVersion(), appVersion(entity.getSoftware())),
                entity.getLastUpdate());
    }

    public Scanner map(final ScannerEntity entity) {
        return new Scanner(
                entity.getId(),
                entity.getExternalDeviceId(),
                entity.getDeviceType(),
                entity.getStatus(),
                mapIdentity(entity.getIdentity()),
                mapHardware(entity.getHardware()),
                mapNetwork(entity.getNetwork()),
                mapOwnership(entity.getUserId(), entity.getDepartmentCode(), entity.getOwnership()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getScanType(),
                entity.getScannerType());
    }

    private DeviceStatus resolveStatus(final DeviceStatus status, final Boolean active) {
        if (status != null) {
            return status;
        }
        return Boolean.TRUE.equals(active) ? DeviceStatus.ACTIVE : DeviceStatus.BLOCKED;
    }

    private String coalesce(final String first, final String second) {
        return first != null ? first : second;
    }

    private String appVersion(final Software software) {
        return software != null ? software.getAppVersion() : null;
    }

    private DeviceIdentity mapIdentity(final Identity identity) {
        if (identity == null) {
            return new DeviceIdentity(null, null, null, null, null, null, null, null);
        }
        return new DeviceIdentity(
                identity.getAssetTag(),
                identity.getBarcode(),
                identity.getExternalSystemId(),
                identity.getHardwareUuid(),
                identity.getImei(),
                identity.getMacAddress(),
                identity.getMdmDeviceId(),
                identity.getSerialNumber());
    }

    private DeviceHardware mapHardware(final Hardware hardware) {
        if (hardware == null) {
            return new DeviceHardware(null, null, null, null, null, null, null, null, null, null, null, null);
        }
        return new DeviceHardware(
                hardware.getCpu(),
                hardware.getHasCamera(),
                hardware.getHasGps(),
                hardware.getHasNfc(),
                hardware.getHasScanner(),
                hardware.getManufacturer(),
                hardware.getModel(),
                hardware.getProductName(),
                hardware.getRamMb(),
                hardware.getRuggedized(),
                hardware.getScreenResolution(),
                hardware.getStorageMb());
    }

    private DeviceSoftware mapSoftware(final Software software, final Set<String> installedApps) {
        if (software == null) {
            return new DeviceSoftware(null, null, null, null, null, installedApps, null, null, null, null);
        }
        return new DeviceSoftware(
                software.getAppVersion(),
                software.getBootloaderVersion(),
                software.getBuildNumber(),
                software.getDeveloperModeEnabled(),
                software.getFirmwareVersion(),
                installedApps,
                software.getKernelVersion(),
                software.getOsName(),
                software.getOsVersion(),
                software.getRooted());
    }

    private DeviceNetwork mapNetwork(final Network network) {
        if (network == null) {
            return new DeviceNetwork(null, null, null, null, null, null, null, null, null);
        }
        return new DeviceNetwork(
                network.getBluetoothMac(),
                network.getCarrier(),
                network.getIpAddress(),
                network.getNetworkType(),
                network.getPublicIpAddress(),
                network.getRoaming(),
                network.getSimSerial(),
                network.getVpnConnected(),
                network.getWifiSsid());
    }

    private SecurityProfile mapSecurity(final Security security) {
        if (security == null) {
            return new SecurityProfile(null, null, null, null, null, null, null, null, null, null);
        }
        return new SecurityProfile(
                security.getBiometricEnabled(),
                security.getCertificateFingerprint(),
                security.getCompromised(),
                security.getEncrypted(),
                security.getFailedLoginAttempts(),
                security.getLastSecurityScanAt(),
                security.getScreenLockEnabled(),
                security.getSecureBootEnabled(),
                security.getSecurityPolicyVersion(),
                security.getTamperDetected());
    }

    private DeviceLocation mapLocation(final Location location) {
        if (location == null) {
            return new DeviceLocation(null, null, null, null, null, null, null, null);
        }
        return new DeviceLocation(
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude(),
                location.getAccuracyMeters(),
                location.getLastKnownAddress(),
                location.getGeoZone(),
                location.getGpsEnabled(),
                location.getLastLocationUpdateAt());
    }

    private OwnershipProfile mapOwnership(final UserId userId,
                                          final DepartmentCode departmentCode,
                                          final Ownership ownership) {
        return new OwnershipProfile(
                ownership != null ? ownership.getAssignedAt() : null,
                ownership != null ? ownership.getAssignedRole() : null,
                departmentCode,
                ownership != null ? ownership.getPreviousUserId() : null,
                ownership != null ? ownership.getTeamId() : null,
                ownership != null ? ownership.getUnassignedAt() : null,
                userId,
                ownership != null ? ownership.getVehicleId() : null);
    }
}
