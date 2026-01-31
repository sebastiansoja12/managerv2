package com.warehouse.terminal.infrastructure.adapter.secondary.mapper;

import java.util.Set;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.vo.HardwareProfile;
import com.warehouse.terminal.domain.vo.IdentityInfo;
import com.warehouse.terminal.domain.vo.LocationProfile;
import com.warehouse.terminal.domain.vo.NetworkProfile;
import com.warehouse.terminal.domain.vo.SecurityProfile;
import com.warehouse.terminal.domain.vo.SoftwareProfile;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Hardware;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Identity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Location;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.MobileEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Network;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Ownership;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.ScannerEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Security;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Software;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.TerminalEntity;

import org.springframework.stereotype.Component;

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
                entity.getLastUpdate(),
                entity.getActive());
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
                entity.getUserId(),
                entity.getDepartmentCode(),
                coalesce(entity.getVersion(), appVersion(entity.getSoftware())),
                entity.getLastUpdate(),
                entity.getActive());
    }

    public Scanner map(final ScannerEntity entity) {
        return new Scanner(
                entity.getId(),
                entity.getExternalDeviceId() != null ? entity.getExternalDeviceId() : ExternalId.generateId(),
                entity.getDeviceType() != null ? entity.getDeviceType() : DeviceType.SCANNER,
                entity.getStatus() != null ? entity.getStatus() : DeviceStatus.ACTIVE,
                mapIdentity(entity.getIdentity()),
                mapHardware(entity.getHardware()),
                null,
                mapNetwork(entity.getNetwork()),
                null,
                null,
                mapOwnership(entity.getUserId(), entity.getDepartmentCode(), entity.getOwnership()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                null,
                null,
                DeviceStatus.ACTIVE.equals(entity.getStatus()),
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

    private IdentityInfo mapIdentity(final Identity identity) {
        if (identity == null) {
            return new IdentityInfo(null, null, null, null, null, null, null, null);
        }
        return new IdentityInfo(
                identity.getAssetTag(),
                identity.getBarcode(),
                identity.getExternalSystemId(),
                identity.getHardwareUuid(),
                identity.getImei(),
                identity.getMacAddress(),
                identity.getMdmDeviceId(),
                identity.getSerialNumber());
    }

    private HardwareProfile mapHardware(final Hardware hardware) {
        if (hardware == null) {
            return new HardwareProfile(null, null, null, null, null, null, null, null, null, null, null, null);
        }
        return new HardwareProfile(
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

    private SoftwareProfile mapSoftware(final Software software, final Set<String> installedApps) {
        if (software == null) {
            return new SoftwareProfile(null, null, null, null, null, installedApps, null, null, null, null);
        }
        return new SoftwareProfile(
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

    private NetworkProfile mapNetwork(final Network network) {
        if (network == null) {
            return new NetworkProfile(null, null, null, null, null, null, null, null, null);
        }
        return new NetworkProfile(
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

    private LocationProfile mapLocation(final Location location) {
        if (location == null) {
            return new LocationProfile(null, null, null, null, null, null, null, null);
        }
        return new LocationProfile(
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
