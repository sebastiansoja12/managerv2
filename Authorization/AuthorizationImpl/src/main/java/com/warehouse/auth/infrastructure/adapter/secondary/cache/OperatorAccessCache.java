package com.warehouse.auth.infrastructure.adapter.secondary.cache;

import com.warehouse.commonassets.identificator.OperatorId;

import java.time.Instant;
import java.util.Optional;

public interface OperatorAccessCache {

    Optional<CachedSessionRevocation> findSessionRevocation(final OperatorId operatorId);

    void putSessionRevocation(final OperatorId operatorId, final CachedSessionRevocation revocation);

    Optional<Boolean> findOperatorActive(final OperatorId operatorId);

    void putOperatorActive(final OperatorId operatorId, final boolean active);

    record CachedSessionRevocation(Instant revokedAt) {

        public static CachedSessionRevocation of(final Optional<Instant> revokedAt) {
            return new CachedSessionRevocation(revokedAt.orElse(null));
        }

        public Optional<Instant> asOptional() {
            return Optional.ofNullable(revokedAt);
        }
    }
}
