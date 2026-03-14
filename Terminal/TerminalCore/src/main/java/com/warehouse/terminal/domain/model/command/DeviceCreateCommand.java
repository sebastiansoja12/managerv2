package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.DeviceUserType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.vo.*;

import lombok.Builder;

@Builder
public class DeviceCreateCommand {
    private UserId userId;
    private SupplierCode supplierCode;
    private String version;
    private DeviceUserType deviceUserType;
    private DepartmentCode departmentCode;
    private DeviceType deviceType;
    private Scanner.ScanType scanType;
    private Scanner.ScannerType scannerType;
    private DeviceIdentity identity;
    private DeviceHardware hardware;
    private DeviceSoftware software;
    private DeviceNetwork network;
    private SecurityProfile security;
    private DeviceLocation location;
    private OwnershipProfile ownership;

    public DeviceCreateCommand(
            final UserId userId,
            final SupplierCode supplierCode,
            final String version,
            final DeviceUserType deviceUserType,
            final DepartmentCode departmentCode,
            final DeviceType deviceType,
            final Scanner.ScanType scanType,
            final Scanner.ScannerType scannerType,
            final DeviceIdentity identity,
            final DeviceHardware hardware,
            final DeviceSoftware software,
            final DeviceNetwork network,
            final SecurityProfile security,
            final DeviceLocation location,
            final OwnershipProfile ownership) {
        this.userId = userId;
        this.supplierCode = supplierCode;
        this.version = version;
        this.deviceUserType = deviceUserType;
        this.departmentCode = departmentCode;
        this.deviceType = deviceType;
        this.scanType = scanType;
        this.scannerType = scannerType;
        this.identity = identity;
        this.hardware = hardware;
        this.software = software;
        this.network = network;
        this.security = security;
        this.location = location;
        this.ownership = ownership;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(final SupplierCode supplierCode) {
        this.supplierCode = supplierCode;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(final UserId userId) {
        this.userId = userId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public DeviceUserType getDeviceUserType() {
        return deviceUserType;
    }

    public DeviceHardware getHardware() {
        return hardware;
    }

    public Scanner.ScanType getScanType() {
        return scanType;
    }

    public Scanner.ScannerType getScannerType() {
        return scannerType;
    }

    public DeviceIdentity getIdentity() {
        return identity;
    }

    public DeviceLocation getLocation() {
        return location;
    }

    public DeviceNetwork getNetwork() {
        return network;
    }

    public OwnershipProfile getOwnership() {
        return ownership;
    }

    public SecurityProfile getSecurity() {
        return security;
    }

    public DeviceSoftware getSoftware() {
        return software;
    }
}
