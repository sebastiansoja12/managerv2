package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.domain.model.ShipmentUpdateCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.enumeration.ShipmentUpdateType;
import com.warehouse.shipment.domain.model.ShipmentCreateCommand;
import com.warehouse.shipment.domain.model.SignatureChangeRequest;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentStatusRequest;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ShipmentRequestMapper {

    ShipmentCreateCommand map(final ShipmentCreateRequestApi requestDto);

    default Money map(final MoneyApi money) {
        if (money == null) {
            return null;
        }
        return new Money(money.getAmount(), Currency.valueOf(money.getCurrency()));
    }

    ShipmentId map(final ShipmentIdDto shipmentId);

    ShipmentUpdateCommand map(final ShipmentUpdateRequestApi request);

    ShipmentUpdateType map(final ShipmentUpdateTypeApi shipmentUpdateType);
    
    Sender mapToSender(final PersonApi person);

    Recipient mapToRecipient(final PersonApi person);

    default ShipmentStatusRequest map(final ShipmentStatusRequestApi shipmentStatusRequest) {
        return new ShipmentStatusRequest(new ShipmentId(shipmentStatusRequest.shipmentId().getValue()), ShipmentStatus.valueOf(shipmentStatusRequest.shipmentStatus().name()));
    }

    default SignatureChangeRequest map(final SignatureChangeRequestApi signatureChangeRequest) {
        final ShipmentId shipmentId = new ShipmentId(signatureChangeRequest.shipmentId().getValue());
        return new SignatureChangeRequest(shipmentId, signatureChangeRequest.signature(), signatureChangeRequest.signerName(),
                signatureChangeRequest.documentReference());
    }
}
