package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.port.secondary.OperatorSessionRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.OperatorSessionRevocationEntity;
import com.warehouse.commonassets.identificator.OperatorId;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class OperatorSessionRepositoryImpl implements OperatorSessionRepository {

    private static final Duration CACHE_TTL = Duration.ofSeconds(15);

    private final RefreshTokenReadRepository refreshTokenReadRepository;

    private final OperatorSessionRevocationReadRepository revocationReadRepository;

    private final Clock clock;

    private final Map<Long, CachedRevocation> revocations = new ConcurrentHashMap<>();

    public OperatorSessionRepositoryImpl(final RefreshTokenReadRepository refreshTokenReadRepository,
                                         final OperatorSessionRevocationReadRepository revocationReadRepository,
                                         final Clock clock) {
        this.refreshTokenReadRepository = refreshTokenReadRepository;
        this.revocationReadRepository = revocationReadRepository;
        this.clock = clock;
    }

    @Override
    public long countActiveRefreshTokens(final OperatorId operatorId, final Instant now) {
        return refreshTokenReadRepository.countActiveForOperator(operatorId.value(),
                LocalDateTime.ofInstant(now, ZoneId.systemDefault()));
    }

    @Override
    public int deleteRefreshTokens(final OperatorId operatorId) {
        return refreshTokenReadRepository.deleteAllForOperator(operatorId.value());
    }

    @Override
    public Optional<Instant> findSessionsRevokedAt(final OperatorId operatorId) {
        final Instant now = Instant.now(clock);
        final CachedRevocation cached = revocations.get(operatorId.value());
        if (cached != null && cached.expiresAt().isAfter(now)) {
            return cached.revokedAt();
        }
        final Optional<Instant> revokedAt = revocationReadRepository.findById(operatorId.value())
                .map(OperatorSessionRevocationEntity::getRevokedAt);
        revocations.put(operatorId.value(), new CachedRevocation(revokedAt, now.plus(CACHE_TTL)));
        return revokedAt;
    }

    @Override
    public void markSessionsRevoked(final OperatorId operatorId, final Instant revokedAt) {
        revocationReadRepository.save(new OperatorSessionRevocationEntity(operatorId.value(), revokedAt));
        revocations.put(operatorId.value(), new CachedRevocation(Optional.of(revokedAt),
                Instant.now(clock).plus(CACHE_TTL)));
    }

    private record CachedRevocation(Optional<Instant> revokedAt, Instant expiresAt) {
    }
}
