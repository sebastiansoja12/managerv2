package com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper;


import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.delivery.dto.*;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponseDetails;
import com.warehouse.deliveryreturn.domain.vo.ReturnToken;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatus;
import com.warehouse.deliveryreturn.infrastructure.api.dto.*;
import com.warehouse.deliveryreturn.infrastructure.api.dto.ShipmentIdDto;
import com.warehouse.terminal.DeviceInformation;

@Mapper
public interface DeliveryReturnResponseMapper {

    DeliveryReturnResponseDto map(final DeliveryReturnResponse response);

    @Mapping(target = "version.value", source = "version")
    @Mapping(target = "username.value", source = "username")
    DeviceInformationDto map(final DeviceInformation deviceInformation);

    default DeliveryReturnResponseDetailsDto map(final DeliveryReturnResponseDetails deliveryReturnResponseDetails) {
        final ProcessIdDto processId = deliveryReturnResponseDetails.getProcessId() == null ? null :
                new ProcessIdDto(UUID.fromString(deliveryReturnResponseDetails.getProcessId().getValue()),
                map(deliveryReturnResponseDetails.getShipmentId()));
        final DeliveryStatusDto deliveryStatus = map(deliveryReturnResponseDetails.getDeliveryStatus());
        final ReturnTokenDto returnToken = map((deliveryReturnResponseDetails.getReturnToken()));
        final ShipmentIdDto shipmentId = map(deliveryReturnResponseDetails.getShipmentId());
        final DepartmentCodeDto departmentCode = map(deliveryReturnResponseDetails.getDepartmentCode());
        final SupplierCodeDto supplierCode = map(deliveryReturnResponseDetails.getSupplierCode());
        final UpdateStatusDto updateStatus = map(deliveryReturnResponseDetails.getUpdateStatus());
        return new DeliveryReturnResponseDetailsDto(processId, shipmentId, departmentCode, supplierCode,
                deliveryStatus, returnToken, updateStatus);
    }

    SupplierCodeDto map(final SupplierCode supplierCode);

    DepartmentCodeDto map(final DepartmentCode departmentCode);

    DeliveryIdDto map(final DeliveryId deliveryId);

    @Mapping(target = "processId", source = "value")
    ProcessIdDto map(final ProcessId processId);

    ReturnTokenDto map(final ReturnToken returnToken);

    ShipmentIdDto map(final ShipmentId shipmentId);

    DeliveryStatusDto map(final DeliveryStatus deliveryStatus);

    UpdateStatusDto map(final UpdateStatus updateStatus);
}
