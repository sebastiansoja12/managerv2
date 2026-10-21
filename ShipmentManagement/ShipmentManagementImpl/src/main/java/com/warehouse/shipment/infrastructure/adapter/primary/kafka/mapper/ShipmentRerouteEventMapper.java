package com.warehouse.shipment.infrastructure.adapter.primary.kafka.mapper;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.reroute.api.event.ShipmentRerouteRequestedIntegrationEvent;
import com.warehouse.reroute.api.event.snapshot.ReroutePartyData;
import com.warehouse.shipment.application.port.primary.command.ShipmentRerouteCommand;
import com.warehouse.shipment.domain.vo.Party;

public class ShipmentRerouteEventMapper {

    public TrackingNumber mapTrackingNumber(final ShipmentRerouteRequestedIntegrationEvent event) {
        return new TrackingNumber(event.trackingNumber());
    }

    public ShipmentRerouteCommand map(final ShipmentRerouteRequestedIntegrationEvent event) {
        return new ShipmentRerouteCommand(
                mapTrackingNumber(event),
                map(event.sender()),
                map(event.recipient()));
    }

    private Party map(final ReroutePartyData party) {
        return new Party(
                party.firstName(),
                party.lastName(),
                party.email(),
                party.telephoneNumber(),
                party.city(),
                party.postalCode(),
                party.street(),
                party.countryCode() != null ? CountryCode.valueOf(party.countryCode()) : null);
    }
}
