package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.reroute.application.port.secondary.ShipmentRerouteServicePort;
import com.warehouse.reroute.domain.vo.ReroutableShipment;
import com.warehouse.reroute.domain.vo.RerouteParty;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRerouteDetailsDto;

import java.util.Optional;

public class ShipmentRerouteServiceAdapter implements ShipmentRerouteServicePort {

    private final ShipmentApiService shipmentApiService;

    public ShipmentRerouteServiceAdapter(final ShipmentApiService shipmentApiService) {
        this.shipmentApiService = shipmentApiService;
    }

    @Override
    public Optional<ReroutableShipment> findShipment(final TrackingNumber trackingNumber) {
        return shipmentApiService.getShipmentForReroute(trackingNumber)
                .map(this::map);
    }

    private ReroutableShipment map(final ShipmentRerouteDetailsDto shipment) {
        return new ReroutableShipment(
                shipment.shipmentId(),
                shipment.trackingNumber(),
                shipment.status(),
                shipment.reroutable(),
                map(shipment.sender()),
                map(shipment.recipient()));
    }

    private RerouteParty map(final ShipmentPartyDto party) {
        return new RerouteParty(party.firstName(), party.lastName(), party.email(), party.telephoneNumber(),
                party.city(), party.postalCode(), party.street(), party.countryCode());
    }
}
