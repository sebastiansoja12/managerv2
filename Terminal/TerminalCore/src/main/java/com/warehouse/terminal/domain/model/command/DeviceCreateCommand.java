package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.DeviceUserType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.OwnershipProfile;
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
    private IdentityInfo identity;
    private HardwareProfile hardware;
    private SoftwareProfile software;
    private NetworkProfile network;
    private SecurityProfile security;
    private LocationProfile location;
    private OwnershipProfile ownership;

    public DeviceCreateCommand(
            final UserId userId,
            final SupplierCode supplierCode,
            final String version,
            final DeviceUserType deviceUserType,
            final DepartmentCode departmentCode,
            final DeviceType deviceType,
            final IdentityInfo identity,
            final HardwareProfile hardware,
            final SoftwareProfile software,
            final NetworkProfile network,
            final SecurityProfile security,
            final LocationProfile location,
            final OwnershipProfile ownership) {
        this.userId = userId;
        this.supplierCode = supplierCode;
        this.version = version;
        this.deviceUserType = deviceUserType;
        this.departmentCode = departmentCode;
        this.deviceType = deviceType;
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

    public HardwareProfile getHardware() {
        return hardware;
    }

    public IdentityInfo getIdentity() {
        return identity;
    }

    public LocationProfile getLocation() {
        return location;
    }

    public NetworkProfile getNetwork() {
        return network;
    }

    public OwnershipProfile getOwnership() {
        return ownership;
    }

    public SecurityProfile getSecurity() {
        return security;
    }

    public SoftwareProfile getSoftware() {
        return software;
    }
}
