package com.warehouse.process.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.DeviceUserType;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.model.DeviceInformation;
import com.warehouse.process.domain.model.InitializeProcessCommand;
import com.warehouse.process.domain.vo.ShipmentUpdated;
import com.warehouse.process.infrastructure.dto.*;

public abstract class RequestMapper {

    public static InitializeProcessCommand map(final InitializeProcessRequestDto request) {
        return InitializeProcessCommand.builder()
                .request(request.request())
                .deviceInformation(map(request.deviceInformation()))
                .build();
    }

    public static DeviceInformation map(final DeviceInformationDto deviceInformation) {
        final DeviceId deviceId = new DeviceId(deviceInformation.deviceId().value());
        final String version = deviceInformation.version();
        final DepartmentCode departmentCode = new DepartmentCode(deviceInformation.departmentCode().value());
        final UserId userId = new UserId(deviceInformation.userId().value());
        final String deviceType = deviceInformation.deviceType().name();
        final String deviceUserType = deviceInformation.deviceUserType().name();
        return new DeviceInformation(deviceId, departmentCode, userId, DeviceType.valueOf(deviceType),
                DeviceUserType.valueOf(deviceUserType), version);
    }

    public static ProcessStatus map(final ProcessStatusDto processStatus) {
        return ProcessStatus.valueOf(processStatus.name());
    }

    public static ProcessId map(final ProcessLogId processLogId) {
        return new ProcessId(processLogId.value());
    }

	public static ShipmentUpdated map(final ShipmentUpdateDto shipmentUpdate) {
		return new ShipmentUpdated(new ShipmentId(shipmentUpdate.shipmentId().value()),
                new DeviceId(shipmentUpdate.deviceId().value()),
				new UserId(shipmentUpdate.createdBy().value()),
				new DepartmentCode(shipmentUpdate.departmentCode().value()), shipmentUpdate.serviceType(),
				shipmentUpdate.processType(), shipmentUpdate.sourceService(), shipmentUpdate.targetService(),
                shipmentUpdate.request(), shipmentUpdate.response());
	}
}
