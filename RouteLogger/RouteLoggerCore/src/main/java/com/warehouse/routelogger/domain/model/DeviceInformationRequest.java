package com.warehouse.routelogger.domain.model;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.routelogger.dto.DeviceDto;
import com.warehouse.routelogger.dto.ProcessTypeDto;
import com.warehouse.routelogger.dto.ShipmentIdDto;

public class DeviceInformationRequest {
    private ShipmentId shipmentId;
    private DeviceId deviceId;
    private DeviceType deviceType;
    private Username username;
    private DepartmentCode departmentCode;
    private String version;
    private ProcessType processType;

    public DeviceInformationRequest(final ShipmentId shipmentId,
                                    final DeviceId deviceId,
                                    final DeviceType deviceType,
                                    final Username username,
                                    final DepartmentCode departmentCode,
                                    final String version,
                                    final ProcessType processType) {
        this.shipmentId = shipmentId;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.username = username;
        this.departmentCode = departmentCode;
        this.version = version;
        this.processType = processType;
    }

    public static DeviceInformationRequest from(final DeviceDto device, final ProcessTypeDto processType,
                                                final ShipmentIdDto shipmentId) {
        final ShipmentId id = new ShipmentId(shipmentId.value());
        final DeviceId deviceId = new DeviceId(id.value());
        final Username username = new Username(device.getUsername().value());
        final DepartmentCode departmentCode = new DepartmentCode(device.getDepartmentCode().value());
        final DeviceType type = DeviceType.valueOf(device.getDeviceType().name());
        final ProcessType typeOfProcess = ProcessType.valueOf(processType.name());
        final String version = device.getDeviceVersion().value();

        return new DeviceInformationRequest(id, deviceId, type, username, departmentCode, version, typeOfProcess);
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public Username getUsername() {
        return username;
    }

    public void setUsername(final Username username) {
        this.username = username;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public ProcessType getProcessType() {
        return processType;
    }
}
