package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import io.micrometer.common.util.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.UpdateType;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentRequest;
import com.warehouse.shipment.domain.vo.ShipmentUpdateRequest;
import com.warehouse.shipment.infrastructure.api.dto.PersonDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentUpdateRequestDto;

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
        return new ShipmentUpdateRequest(shipmentId, shipmentUpdate, UpdateType.AUTO, processType);
    }
    
    Sender mapToSender(final PersonDto person);

    Recipient mapToRecipient(final PersonDto person);

    default ProcessType processDeterminer(final String token) {

        if (StringUtils.isEmpty(token)) {
            return ProcessType.CREATED;
        }

        return switch (token.length()) {
            case 8 -> ProcessType.REDIRECT;
            case 6 -> ProcessType.REROUTE;
            case 4 -> ProcessType.ROUTE;
            default -> throw new IllegalStateException("Unexpected value: " + token.length());
        };
    }
}
