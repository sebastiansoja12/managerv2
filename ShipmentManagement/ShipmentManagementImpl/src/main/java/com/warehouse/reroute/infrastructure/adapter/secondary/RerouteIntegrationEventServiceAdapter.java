package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.reroute.api.event.ShipmentRerouteRequestedIntegrationEvent;
import com.warehouse.reroute.api.event.snapshot.ReroutePartyData;
import com.warehouse.reroute.application.port.secondary.RerouteIntegrationEventServicePort;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.vo.ReroutableShipment;
import com.warehouse.reroute.domain.vo.RerouteParty;

import java.time.Instant;

public class RerouteIntegrationEventServiceAdapter implements RerouteIntegrationEventServicePort {

    private final IntegrationEventPublisher integrationEventPublisher;

    public RerouteIntegrationEventServiceAdapter(final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @Override
    public void publishRerouteRequested(final ReroutableShipment shipment,
                                        final RerouteToken rerouteToken,
                                        final RerouteParty sender,
                                        final RerouteParty recipient,
                                        final Instant requestedAt) {
        integrationEventPublisher.publish(new ShipmentRerouteRequestedIntegrationEvent(
                rerouteToken.getRerouteTokenId() == null ? null : rerouteToken.getRerouteTokenId().value(),
                shipment.shipmentId(),
                shipment.trackingNumber().value(),
                map(sender),
                map(recipient),
                requestedAt));
    }

    private ReroutePartyData map(final RerouteParty party) {
        return new ReroutePartyData(party.firstName(), party.lastName(), party.email(), party.telephoneNumber(),
                party.city(), party.postalCode(), party.street(), party.countryCode());
    }
}
