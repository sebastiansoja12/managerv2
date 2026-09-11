package com.warehouse.mcp.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.application.port.secondary.ShipmentServicePort;
import com.warehouse.mcp.domain.vo.ShipmentEvent;
import com.warehouse.mcp.domain.vo.ShipmentParty;
import com.warehouse.mcp.domain.vo.ShipmentTracking;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentTrackingDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentTrackingEventDto;

public class ShipmentServiceAdapter implements ShipmentServicePort {

    private final ShipmentApiService shipmentApiService;

    public ShipmentServiceAdapter(final ShipmentApiService shipmentApiService) {
        this.shipmentApiService = shipmentApiService;
    }

    @Override
    public ShipmentTracking findShipmentTracking(final TrackingNumber trackingNumber) {
        final ShipmentTrackingDto tracking = this.shipmentApiService.getShipmentTracking(trackingNumber);
        return new ShipmentTracking(
                tracking.shipmentId(),
                tracking.trackingNumber(),
                tracking.status(),
                tracking.createdAt(),
                tracking.updatedAt(),
                map(tracking.sender()),
                map(tracking.recipient()),
                tracking.events().stream().map(this::map).toList());
    }

    private ShipmentParty map(final ShipmentPartyDto party) {
        if (party == null) {
            return null;
        }
        return new ShipmentParty(party.firstName(), party.lastName(), party.email(), party.telephoneNumber(),
                party.city(), party.postalCode(), party.countryCode());
    }

    private ShipmentEvent map(final ShipmentTrackingEventDto event) {
        return new ShipmentEvent(event.timestamp(), event.status(), event.processType(), event.processId(),
                event.departmentCode(), event.username(), event.description());
    }
}
