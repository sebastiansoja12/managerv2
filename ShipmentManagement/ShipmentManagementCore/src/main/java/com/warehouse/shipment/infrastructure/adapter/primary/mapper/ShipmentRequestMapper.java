package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.domain.model.SignatureChangeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.ShipmentUpdateType;
import com.warehouse.shipment.domain.enumeration.UpdateType;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentStatusRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.*;

import io.micrometer.common.util.StringUtils;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ShipmentRequestMapper {

    ShipmentRequest map(final ShipmentRequestDto requestDto);

    ShipmentRequestDto map(final ShipmentRequest request);

    ShipmentId map(final ShipmentIdDto shipmentId);

    default ShipmentUpdateRequest map(final ShipmentUpdateRequestDto request) {
        final ProcessType processType = processDeterminer(request.getToken());
        final ShipmentId shipmentId = map(request.getShipmentId());
        final Sender sender = mapToSender(request.getSender());
        final Recipient recipient = mapToRecipient(request.getRecipient());
		final ShipmentUpdate shipmentUpdate = new ShipmentUpdate(sender, recipient,
				request.getDestination(), request.getToken());
        return new ShipmentUpdateRequest(shipmentId, shipmentUpdate, UpdateType.AUTO, processType,
                map(request.getShipmentUpdateType()));
    }

    ShipmentUpdateType map(final ShipmentUpdateTypeDto shipmentUpdateType);
    
    Sender mapToSender(final PersonDto person);

    Recipient mapToRecipient(final PersonDto person);

    default ProcessType processDeterminer(final String token) {

        if (StringUtils.isEmpty(token)) {
            return ProcessType.CREATED;
        }

        return switch (token.length()) {
            case 8 -> ProcessType.REDIRECT;
            case 9 -> ProcessType.REROUTE;
            case 4 -> ProcessType.ROUTE;
            default -> throw new IllegalStateException("Unexpected value: " + token.length());
        };
    }

    ShipmentStatusRequest map(final ShipmentStatusRequestDto shipmentStatusRequest);

    SignatureChangeRequest map(final SignatureChangeRequestDto signatureChangeRequest);
}
