package com.warehouse.reroute.application.port.secondary;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.domain.model.RerouteToken;

public interface RerouteTokenRepository {

    RerouteToken save(final RerouteToken rerouteToken);

    /** Locks the token row until the surrounding transaction ends, so failed attempts are counted one by one. */
    Optional<RerouteToken> findLatestByShipmentIdForUpdate(final ShipmentId shipmentId);

    long countIssuedSince(final ShipmentId shipmentId, final Instant since);

    List<RerouteToken> findActiveByShipmentId(final ShipmentId shipmentId, final Instant now);

    void deleteExpiredBefore(final Instant expiredBefore);
}
