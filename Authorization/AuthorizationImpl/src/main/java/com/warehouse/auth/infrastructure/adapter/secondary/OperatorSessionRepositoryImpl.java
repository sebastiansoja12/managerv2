package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.port.secondary.OperatorSessionRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.cache.OperatorAccessCache;
import com.warehouse.auth.infrastructure.adapter.secondary.cache.OperatorAccessCache.CachedSessionRevocation;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.OperatorSessionRevocationEntity;
import com.warehouse.commonassets.identificator.OperatorId;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class OperatorSessionRepositoryImpl implements OperatorSessionRepository {

    private final RefreshTokenReadRepository refreshTokenReadRepository;

    private final OperatorSessionRevocationReadRepository revocationReadRepository;

    private final OperatorAccessCache operatorAccessCache;

    public OperatorSessionRepositoryImpl(final RefreshTokenReadRepository refreshTokenReadRepository,
                                         final OperatorSessionRevocationReadRepository revocationReadRepository,
                                         final OperatorAccessCache operatorAccessCache) {
        this.refreshTokenReadRepository = refreshTokenReadRepository;
        this.revocationReadRepository = revocationReadRepository;
        this.operatorAccessCache = operatorAccessCache;
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
        return operatorAccessCache.findSessionRevocation(operatorId)
                .map(CachedSessionRevocation::asOptional)
                .orElseGet(() -> {
                    final Optional<Instant> revokedAt = revocationReadRepository.findById(operatorId.value())
                            .map(OperatorSessionRevocationEntity::getRevokedAt);
                    operatorAccessCache.putSessionRevocation(operatorId, CachedSessionRevocation.of(revokedAt));
                    return revokedAt;
                });
    }

    @Override
    public void markSessionsRevoked(final OperatorId operatorId, final Instant revokedAt) {
        revocationReadRepository.save(new OperatorSessionRevocationEntity(operatorId.value(), revokedAt));
        operatorAccessCache.putSessionRevocation(operatorId, new CachedSessionRevocation(revokedAt));
    }
}
