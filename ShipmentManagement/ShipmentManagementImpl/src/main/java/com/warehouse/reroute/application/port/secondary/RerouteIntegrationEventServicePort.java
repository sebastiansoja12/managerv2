package com.warehouse.reroute.application.port.secondary;

import java.time.Instant;

import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.vo.ReroutableShipment;
import com.warehouse.reroute.domain.vo.RerouteParty;

public interface RerouteIntegrationEventServicePort {

    void publishRerouteRequested(final ReroutableShipment shipment,
                                 final RerouteToken rerouteToken,
                                 final RerouteParty sender,
                                 final RerouteParty recipient,
                                 final Instant requestedAt);
}
