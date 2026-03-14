package com.warehouse.terminal.infrastructure.adapter.secondary.mapper;

import java.time.Instant;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.vo.DeviceHardware;
import com.warehouse.terminal.domain.vo.DeviceIdentity;
import com.warehouse.terminal.domain.vo.DeviceLocation;
import com.warehouse.terminal.domain.vo.DeviceNetwork;
import com.warehouse.terminal.domain.vo.SecurityProfile;
import com.warehouse.terminal.domain.vo.DeviceSoftware;
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
public class ModelToEntityMapper {

    public TerminalEntity map(final Terminal terminal) {
        final TerminalEntity entity = new TerminalEntity();
        fillBase(entity, terminal);
        entity.setUserId(terminal.getUserId());
        entity.setDepartmentCode(terminal.getDepartmentCode());
        entity.setVersion(terminal.getVersion());
        entity.setLastUpdate(terminal.getLastUpdate() != null ? terminal.getLastUpdate() : entity.getUpdatedAt());
        entity.setActive(terminal.isActive());
        entity.setIdentity(mapIdentity(terminal.getIdentity()));
        entity.setHardware(mapHardware(terminal.getHardware()));
        entity.setSoftware(mapSoftware(terminal.getSoftware()));
        entity.setNetwork(mapNetwork(terminal.getNetwork()));
        entity.setSecurity(mapSecurity(terminal.getSecurity()));
        entity.setLocation(mapLocation(terminal.getLocation()));
        entity.setOwnership(mapOwnership(terminal.getOwnership()));
        return entity;
    }

    public MobileEntity map(final Mobile mobile) {
        final MobileEntity entity = new MobileEntity();
        fillBase(entity, mobile);
        entity.setUserId(mobile.getUserId());
        entity.setDepartmentCode(mobile.getDepartmentCode());
        entity.setVersion(mobile.getVersion());
        entity.setLastUpdate(mobile.getLastUpdate() != null ? mobile.getLastUpdate() : entity.getUpdatedAt());
        entity.setActive(mobile.isActive());
        entity.setIdentity(mapIdentity(mobile.getIdentity()));
        entity.setHardware(mapHardware(mobile.getHardware()));
        entity.setSoftware(mapSoftware(mobile.getSoftware()));
        entity.setInstalledApps(mobile.getSoftware() != null ? mobile.getSoftware().getInstalledApps() : null);
        entity.setNetwork(mapNetwork(mobile.getNetwork()));
        entity.setSecurity(mapSecurity(mobile.getSecurity()));
        entity.setLocation(mapLocation(mobile.getLocation()));
        entity.setOwnership(mapOwnership(mobile.getOwnership()));
        return entity;
    }

    public ScannerEntity map(final Scanner scanner) {
        final ScannerEntity entity = new ScannerEntity();
        fillBase(entity, scanner);
        entity.setUserId(resolveScannerUserId(scanner));
        entity.setDepartmentCode(resolveScannerDepartmentCode(scanner));
        entity.setIdentity(mapIdentity(scanner.getIdentity()));
        entity.setHardware(mapHardware(scanner.getHardware()));
        entity.setNetwork(mapNetwork(scanner.getNetwork()));
        entity.setOwnership(mapOwnership(scanner.getOwnership()));
        entity.setScanType(scanner.getScanType());
        entity.setScannerType(scanner.getScannerType());
        return entity;
    }

    private void fillBase(final TerminalEntity entity, final Terminal terminal) {
        entity.setId(terminal.getDeviceId());
        entity.setExternalDeviceId(terminal.getExternalDeviceId() != null ? terminal.getExternalDeviceId() : ExternalId.generateId());
        entity.setDeviceType(terminal.getDeviceType());
        entity.setStatus(terminal.getStatus());
        entity.setCreatedAt(terminal.getCreatedAt() != null ? terminal.getCreatedAt() : Instant.now());
        entity.setUpdatedAt(terminal.getUpdatedAt() != null ? terminal.getUpdatedAt() : Instant.now());
    }

    private void fillBase(final MobileEntity entity, final Mobile mobile) {
        entity.setId(mobile.getDeviceId());
        entity.setExternalDeviceId(mobile.getExternalDeviceId() != null ? mobile.getExternalDeviceId() : ExternalId.generateId());
        entity.setDeviceType(mobile.getDeviceType());
        entity.setStatus(mobile.getStatus());
        entity.setCreatedAt(mobile.getCreatedAt() != null ? mobile.getCreatedAt() : Instant.now());
        entity.setUpdatedAt(mobile.getUpdatedAt() != null ? mobile.getUpdatedAt() : Instant.now());
    }

    private void fillBase(final ScannerEntity entity, final Scanner scanner) {
        entity.setId(scanner.getDeviceId());
        entity.setExternalDeviceId(scanner.getExternalDeviceId() != null ? scanner.getExternalDeviceId() : ExternalId.generateId());
        entity.setDeviceType(scanner.getDeviceType());
        entity.setStatus(scanner.getStatus());
        entity.setCreatedAt(scanner.getCreatedAt() != null ? scanner.getCreatedAt() : Instant.now());
        entity.setUpdatedAt(scanner.getUpdatedAt() != null ? scanner.getUpdatedAt() : Instant.now());
    }

    private UserId resolveScannerUserId(final Scanner scanner) {
        if (scanner.getOwnership() != null && scanner.getOwnership().getUserId() != null) {
            return scanner.getOwnership().getUserId();
        }
        return scanner.getUserId();
    }

    private DepartmentCode resolveScannerDepartmentCode(final Scanner scanner) {
        if (scanner.getOwnership() != null && scanner.getOwnership().getDepartmentCode() != null) {
            return scanner.getOwnership().getDepartmentCode();
        }
        return scanner.getDepartmentCode();
    }

    private Identity mapIdentity(final DeviceIdentity identity) {
        if (identity == null) {
            return null;
        }
        return new Identity(
                identity.getHardwareUuid(),
                identity.getSerialNumber(),
                identity.getImei(),
                identity.getMacAddress(),
                identity.getAssetTag(),
                identity.getBarcode(),
                identity.getExternalSystemId(),
                identity.getMdmDeviceId());
    }

    private Hardware mapHardware(final DeviceHardware hardware) {
        if (hardware == null) {
            return null;
        }
        return new Hardware(
                hardware.getManufacturer(),
                hardware.getModel(),
                hardware.getProductName(),
                hardware.getCpu(),
                hardware.getRamMb(),
                hardware.getStorageMb(),
                hardware.getScreenResolution(),
                hardware.getHasScanner(),
                hardware.getHasCamera(),
                hardware.getHasNfc(),
                hardware.getHasGps(),
                hardware.getRuggedized());
    }

    private Software mapSoftware(final DeviceSoftware software) {
        if (software == null) {
            return null;
        }
        return new Software(
                software.getOsName(),
                software.getOsVersion(),
                software.getFirmwareVersion(),
                software.getKernelVersion(),
                software.getBootloaderVersion(),
                software.getAppVersion(),
                software.getBuildNumber(),
                software.getRooted(),
                software.getDeveloperModeEnabled());
    }

    private Network mapNetwork(final DeviceNetwork network) {
        if (network == null) {
            return null;
        }
        return new Network(
                network.getIpAddress(),
                network.getPublicIpAddress(),
                network.getNetworkType(),
                network.getCarrier(),
                network.getSimSerial(),
                network.getRoaming(),
                network.getVpnConnected(),
                network.getWifiSsid(),
                network.getBluetoothMac());
    }

    private Security mapSecurity(final SecurityProfile security) {
        if (security == null) {
            return null;
        }
        return new Security(
                security.getEncrypted(),
                security.getSecureBootEnabled(),
                security.getTamperDetected(),
                security.getScreenLockEnabled(),
                security.getBiometricEnabled(),
                security.getCompromised(),
                security.getFailedLoginAttempts(),
                security.getLastSecurityScanAt(),
                security.getSecurityPolicyVersion(),
                security.getCertificateFingerprint());
    }

    private Location mapLocation(final DeviceLocation location) {
        if (location == null) {
            return null;
        }
        return new Location(
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude(),
                location.getAccuracyMeters(),
                location.getLastKnownAddress(),
                location.getGeoZone(),
                location.getGpsEnabled(),
                location.getLastLocationUpdateAt());
    }

    private Ownership mapOwnership(final OwnershipProfile ownership) {
        if (ownership == null) {
            return null;
        }
        return new Ownership(
                ownership.getPreviousUserId(),
                ownership.getTeamId(),
                ownership.getVehicleId(),
                ownership.getAssignedRole(),
                ownership.getAssignedAt(),
                ownership.getUnassignedAt());
    }
}
