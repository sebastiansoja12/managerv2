package com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper;


import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.delivery.domain.vo.ReturnToken;
import com.warehouse.delivery.dto.DeliveryIdDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponseDetails;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatus;
import com.warehouse.deliveryreturn.infrastructure.api.dto.*;

import java.util.UUID;

@Mapper
public interface DeliveryReturnResponseMapper {

    DeliveryReturnResponseDto map(final DeliveryReturnResponse response);

    @Mapping(target = "version.value", source = "version")
    @Mapping(target = "username.value", source = "username")
    DeviceInformationDto map(final DeviceInformation deviceInformation);

    default DeliveryReturnResponseDetailsDto map(final DeliveryReturnResponseDetails deliveryReturnResponseDetails) {
        final ProcessIdDto processId = new ProcessIdDto(UUID.fromString(deliveryReturnResponseDetails.getProcessId().getValue()),
                map(deliveryReturnResponseDetails.getShipmentId()));
        final DeliveryStatusDto deliveryStatus = map(deliveryReturnResponseDetails.getDeliveryStatus());
        final ReturnTokenDto returnToken = map((deliveryReturnResponseDetails.getReturnToken()));
        final DeliveryIdDto deliveryId = map(deliveryReturnResponseDetails.getDeliveryId());
        return new DeliveryReturnResponseDetailsDto(processId, deliveryId, deliveryStatus, returnToken);
    }

    DeliveryIdDto map(final DeliveryId deliveryId);

    @Mapping(target = "processId", source = "value")
    ProcessIdDto map(final ProcessId processId);

    ReturnTokenDto map(final ReturnToken returnToken);

    ShipmentIdDto map(final ShipmentId shipmentId);

    DeliveryStatusDto map(final DeliveryStatus deliveryStatus);

    UpdateStatusDto map(final UpdateStatus updateStatus);
}
