package com.warehouse.routetracker.domain.model;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.DeviceInformationRequestDto;

public class DeviceInformationRequest {
    private ShipmentId shipmentId;
    private DeviceId deviceId;
    private DeviceType deviceType;
    private UserId userId;
    private DepartmentCode departmentCode;
    private String version;
    private ProcessType processType;

    public DeviceInformationRequest(final ShipmentId shipmentId,
                                    final DeviceId deviceId,
                                    final DeviceType deviceType,
                                    final UserId userId,
                                    final DepartmentCode departmentCode,
                                    final String version,
                                    final ProcessType processType) {
        this.shipmentId = shipmentId;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.userId = userId;
        this.departmentCode = departmentCode;
        this.version = version;
        this.processType = processType;
    }

    public static DeviceInformationRequest from(final DeviceInformationRequestDto device) {
        final ShipmentId shipmentId = new ShipmentId(device.shipmentId().getValue());
        final DeviceId deviceId = new DeviceId(device.deviceId().value());
        final DeviceType deviceType = DeviceType.valueOf(device.deviceType().name());
        final UserId userId = new UserId(device.userId().value());
        final DepartmentCode departmentCode = new DepartmentCode(device.departmentCode().value());
        final String version = device.deviceVersion().value();
        final ProcessType processType = ProcessType.valueOf(device.processType().name());
        return new DeviceInformationRequest(shipmentId, deviceId, deviceType, userId, departmentCode, version, processType);
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

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(final UserId userId) {
        this.userId = userId;
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
