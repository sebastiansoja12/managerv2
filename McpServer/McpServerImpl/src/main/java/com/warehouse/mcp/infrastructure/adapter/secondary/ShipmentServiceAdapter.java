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
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.mcp.domain.vo.PartyRole;
import com.warehouse.mcp.domain.vo.ShipmentDraft;
import com.warehouse.shipment.infrastructure.ShipmentManagementApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentCreateApiRequestDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyRequestDto;

public class ShipmentServiceAdapter implements ShipmentServicePort {

    private final ShipmentApiService shipmentApiService;

    private final ShipmentManagementApiService shipmentManagementApiService;

    public ShipmentServiceAdapter(final ShipmentApiService shipmentApiService,
                                  final ShipmentManagementApiService shipmentManagementApiService) {
        this.shipmentApiService = shipmentApiService;
        this.shipmentManagementApiService = shipmentManagementApiService;
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

    @Override
    public TrackingNumber createShipment(final ShipmentDraft shipment) {
        return this.shipmentManagementApiService.createShipment(new ShipmentCreateApiRequestDto(
                map(shipment.sender()), map(shipment.recipient()), shipment.lengthCm(), shipment.widthCm(),
                shipment.heightCm(), shipment.weightKg(), shipment.contentDescription(), shipment.price(),
                shipment.declaredValue(), shipment.currency(), shipment.customerReference(), shipment.priority(),
                shipment.packagingType(), shipment.serviceLevel(), shipment.pickupMethod(),
                shipment.deliveryMethod(), shipment.pickupPointId(), shipment.deliveryPickupPointId()))
                .trackingNumber();
    }

    @Override
    public void changeParty(final ShipmentId shipmentId, final PartyRole role, final ShipmentParty party) {
        if (role == PartyRole.SENDER) {
            this.shipmentManagementApiService.changeSender(shipmentId, map(party));
        } else {
            this.shipmentManagementApiService.changeRecipient(shipmentId, map(party));
        }
    }

    private ShipmentPartyRequestDto map(final ShipmentParty party) {
        if (party == null) {
            return null;
        }
        return new ShipmentPartyRequestDto(party.firstName(), party.lastName(), party.email(),
                party.telephoneNumber(), party.city(), party.postalCode(), party.street(), party.countryCode());
    }

    private ShipmentParty map(final ShipmentPartyDto party) {
        if (party == null) {
            return null;
        }
        return new ShipmentParty(party.firstName(), party.lastName(), party.email(), party.telephoneNumber(),
                party.city(), party.postalCode(), party.street(), party.countryCode());
    }

    private ShipmentEvent map(final ShipmentTrackingEventDto event) {
        return new ShipmentEvent(event.timestamp(), event.status(), event.processType(), event.processId(),
                event.departmentCode(), event.username(), event.description());
    }
}
