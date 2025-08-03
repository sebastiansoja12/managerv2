package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.model.Money;
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

    default Money map(final MoneyDto money) {
        if (money == null) {
            return null;
        }
        return new Money(money.getAmount(), Currency.valueOf(money.getCurrency()));
    }

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

    default SignatureChangeRequest map(final SignatureChangeRequestDto signatureChangeRequest) {
        final ShipmentId shipmentId = new ShipmentId(signatureChangeRequest.shipmentId().getValue());
        return new SignatureChangeRequest(shipmentId, signatureChangeRequest.signature(), signatureChangeRequest.signerName(),
                signatureChangeRequest.documentReference());
    }
}
