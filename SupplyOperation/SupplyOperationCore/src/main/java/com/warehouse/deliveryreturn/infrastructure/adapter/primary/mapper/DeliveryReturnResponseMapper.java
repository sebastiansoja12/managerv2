package com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper;


import org.mapstruct.Mapper;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponseDetails;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatus;
import com.warehouse.deliveryreturn.infrastructure.api.dto.*;

@Mapper
public interface DeliveryReturnResponseMapper {

    DeliveryReturnResponseDto map(final DeliveryReturnResponse response);

    default DeliveryReturnResponseDetailsDto map(final DeliveryReturnResponseDetails deliveryReturnResponseDetails) {
        final ProcessIdDto processId = new ProcessIdDto(deliveryReturnResponseDetails.getId(),
                map(deliveryReturnResponseDetails.getShipmentId()));
        final DeliveryStatusDto deliveryStatus = map(deliveryReturnResponseDetails.getDeliveryStatus());
        final ReturnTokenDto returnToken = new ReturnTokenDto(deliveryReturnResponseDetails.getReturnToken());
        final UpdateStatusDto updateStatus = map(deliveryReturnResponseDetails.getUpdateStatus());
        return new DeliveryReturnResponseDetailsDto(processId, deliveryStatus, returnToken, updateStatus);
    }

    ShipmentIdDto map(final ShipmentId shipmentId);

    DeliveryStatusDto map(final DeliveryStatus deliveryStatus);

    UpdateStatusDto map(final UpdateStatus updateStatus);
}
