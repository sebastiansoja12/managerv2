package com.warehouse.reroute.application.port.secondary;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.domain.model.RerouteToken;

public interface RerouteTokenRepository {

    RerouteToken save(final RerouteToken rerouteToken);

    Optional<RerouteToken> findLatestByShipmentId(final ShipmentId shipmentId);

    List<RerouteToken> findActiveByShipmentId(final ShipmentId shipmentId, final Instant now);

    void deleteExpiredBefore(final Instant expiredBefore);
}
