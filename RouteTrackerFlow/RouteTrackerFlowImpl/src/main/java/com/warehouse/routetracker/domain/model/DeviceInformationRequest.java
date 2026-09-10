package com.warehouse.routetracker.domain.model;

import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.domain.vo.DeviceId;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.DeviceInformationRequestDto;

public class DeviceInformationRequest {
    private ShipmentId shipmentId;
    private DeviceId deviceId;
    private String deviceType;
    private UserId userId;
    private DepartmentId departmentId;
    private String version;
    private ProcessType processType;

    public DeviceInformationRequest(final ShipmentId shipmentId,
                                    final DeviceId deviceId,
                                    final String deviceType,
                                    final UserId userId,
                                    final DepartmentId departmentId,
                                    final String version,
                                    final ProcessType processType) {
        this.shipmentId = shipmentId;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.userId = userId;
        this.departmentId = departmentId;
        this.version = version;
        this.processType = processType;
    }

    public static DeviceInformationRequest from(final DeviceInformationRequestDto device) {
        final ShipmentId shipmentId = new ShipmentId(device.shipmentId().getValue());
        final DeviceId deviceId = new DeviceId(device.deviceId().value());
        final String deviceType = device.deviceType().name();
        final UserId userId = new UserId(device.userId().value());
        final DepartmentId departmentId = new DepartmentId(device.departmentId().value());
        final String version = device.deviceVersion().value();
        final ProcessType processType = ProcessType.valueOf(device.processType().name());
        return new DeviceInformationRequest(shipmentId, deviceId, deviceType, userId, departmentId, version, processType);
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

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(final String deviceType) {
        this.deviceType = deviceType;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(final UserId userId) {
        this.userId = userId;
    }

    public DepartmentId getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final DepartmentId departmentId) {
        this.departmentId = departmentId;
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
