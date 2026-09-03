package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.application.port.primary.result.ShipmentRerouteResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRerouteDetailsDto;
import org.springframework.stereotype.Component;

@Component
public class ShipmentApiServiceMapper {

    public ShipmentDetailsDto map(final ShipmentResult shipment) {
        return new ShipmentDetailsDto(
                shipment.snapshot().shipmentId(),
                shipment.snapshot().shipmentStatus(),
                shipment.snapshot().shipmentRelatedId()
        );
    }

    public ShipmentRerouteDetailsDto map(final ShipmentRerouteResult shipmentRerouteResult) {
        final ShipmentSnapshot snapshot = shipmentRerouteResult.snapshot();
        return new ShipmentRerouteDetailsDto(
                snapshot.shipmentId(),
                snapshot.trackingNumber(),
                snapshot.shipmentStatus(),
                shipmentRerouteResult.reroutable(),
                map(snapshot.sender()),
                map(snapshot.recipient())
        );
    }

    private ShipmentPartyDto map(final Party party) {
        return new ShipmentPartyDto(
                party.getFirstName(),
                party.getLastName(),
                party.getEmail(),
                party.getTelephoneNumber(),
                party.getCity(),
                party.getPostalCode(),
                party.getStreet(),
                party.getCountryCode() != null ? party.getCountryCode().name() : null
        );
    }
}
