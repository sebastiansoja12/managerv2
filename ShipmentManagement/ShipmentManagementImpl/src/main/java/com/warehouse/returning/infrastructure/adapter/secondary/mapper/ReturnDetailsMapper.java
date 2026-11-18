package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.returning.api.dto.*;
import com.warehouse.returning.application.port.secondary.DepartmentServicePort;
import com.warehouse.returning.application.port.secondary.ShipmentServicePort;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ReturnPackageApi;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ReturnPageApi;

public class ReturnDetailsMapper {
    private final DepartmentServicePort departmentServicePort;
    private final ShipmentServicePort shipmentServicePort;

    public ReturnDetailsMapper(final DepartmentServicePort departmentServicePort,
                               final ShipmentServicePort shipmentServicePort) {
        this.departmentServicePort = departmentServicePort;
        this.shipmentServicePort = shipmentServicePort;
    }

    public ReturnDetailsDto map(final ReturnPackageApi response) {
        return new ReturnDetailsDto(new LongValueDto(response.returnPackageId().value()),
                new LongValueDto(response.shipmentId().value()),
                trackingNumber(new ShipmentId(response.shipmentId().value())),
                response.returnShipmentId() == null ? null : new LongValueDto(response.returnShipmentId().value()),
                response.returnShipmentId() == null ? null
                        : trackingNumber(new ShipmentId(response.returnShipmentId().value())),
                response.reason(),
                ReturnStatus.valueOf(response.returnStatus()),
                response.returnToken() == null ? null : new StringValueDto(response.returnToken().value()),
                response.assignedDepartmentId(), response.returnedDepartmentId(),
                code(response.assignedDepartmentId()), code(response.returnedDepartmentId()),
                response.assignedTo() == null ? null : new LongValueDto(response.assignedTo().value()),
                response.processedBy() == null ? null : new LongValueDto(response.processedBy().value()),
                response.reasonCode() == null ? null : new StringValueDto(response.reasonCode().value()),
                response.operatorId(), response.createdAt(), response.updatedAt());
    }

    public ReturnPageDto map(final ReturnPageApi response) {
        return new ReturnPageDto(response.content().stream().map(this::map).toList(), response.page(),
                response.size(), response.totalElements(), response.totalPages());
    }

    private StringValueDto trackingNumber(final ShipmentId shipmentId) {
        final TrackingNumber trackingNumber = shipmentServicePort.getShipment(shipmentId).trackingNumber();
        return trackingNumber == null ? null : new StringValueDto(trackingNumber.value());
    }

    private StringValueDto code(final DepartmentId departmentId) {
        return departmentId == null ? null : new StringValueDto(departmentServicePort.getCode(departmentId));
    }
}
