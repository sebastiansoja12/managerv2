package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.ShipmentUpdateType;
import com.warehouse.shipment.domain.enumeration.UpdateType;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.model.SignatureChangeRequest;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ShipmentRequestMapper {

    ShipmentCreateRequest map(final ShipmentCreateRequestDto requestDto);

    ShipmentId map(final ShipmentIdDto shipmentId);

    default ShipmentUpdateRequest map(final ShipmentUpdateRequestDto request) {
        final ShipmentId shipmentId = map(request.getShipmentId());
        final Sender sender = mapToSender(request.getSender());
        final Recipient recipient = mapToRecipient(request.getRecipient());
		final ShipmentUpdate shipmentUpdate = new ShipmentUpdate(sender, recipient,
				request.getDestination(), request.getToken());
        return new ShipmentUpdateRequest(shipmentId, shipmentUpdate, UpdateType.AUTO, null,
                map(request.getShipmentUpdateType()));
    }

    ShipmentUpdateType map(final ShipmentUpdateTypeDto shipmentUpdateType);
    
    Sender mapToSender(final PersonDto person);

    Recipient mapToRecipient(final PersonDto person);

    ShipmentStatusRequest map(final ShipmentStatusRequestDto shipmentStatusRequest);

    SignatureChangeRequest map(final SignatureChangeRequestDto signatureChangeRequest);
}
