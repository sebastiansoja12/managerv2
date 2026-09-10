package com.warehouse.reroute.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.api.event.snapshot.ReroutePartyData;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "shipment.reroute.requested", version = 1)
public record ShipmentRerouteRequestedIntegrationEvent(Long rerouteTokenId,
                                                       ShipmentId shipmentId,
                                                       String trackingNumber,
                                                       ReroutePartyData sender,
                                                       ReroutePartyData recipient,
                                                       Instant requestedAt) implements IntegrationEvent, IntegrationEventKey {

    @JsonCreator
    public ShipmentRerouteRequestedIntegrationEvent(@JsonProperty("rerouteTokenId") final Long rerouteTokenId,
                                                    @JsonProperty("shipmentId") final ShipmentId shipmentId,
                                                    @JsonProperty("trackingNumber") final String trackingNumber,
                                                    @JsonProperty("sender") final ReroutePartyData sender,
                                                    @JsonProperty("recipient") final ReroutePartyData recipient,
                                                    @JsonProperty("requestedAt") final Instant requestedAt) {
        this.rerouteTokenId = rerouteTokenId;
        this.shipmentId = shipmentId;
        this.trackingNumber = trackingNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.requestedAt = requestedAt;
    }

    @Override
    public String eventKey() {
        return trackingNumber;
    }
}
