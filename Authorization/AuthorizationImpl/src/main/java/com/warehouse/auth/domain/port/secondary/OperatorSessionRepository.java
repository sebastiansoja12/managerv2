package com.warehouse.auth.domain.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;

import java.time.Instant;
import java.util.Optional;

public interface OperatorSessionRepository {

    long countActiveRefreshTokens(final OperatorId operatorId, final Instant now);

    int deleteRefreshTokens(final OperatorId operatorId);

    Optional<Instant> findSessionsRevokedAt(final OperatorId operatorId);

    void markSessionsRevoked(final OperatorId operatorId, final Instant revokedAt);
}
