package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.port.secondary.OperatorSessionRepository;
import com.warehouse.commonassets.identificator.OperatorId;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;

@Slf4j
public class OperatorSessionPortImpl implements OperatorSessionPort {

    private final OperatorSessionRepository operatorSessionRepository;

    private final Clock clock;

    public OperatorSessionPortImpl(final OperatorSessionRepository operatorSessionRepository, final Clock clock) {
        this.operatorSessionRepository = operatorSessionRepository;
        this.clock = clock;
    }

    @Override
    public long countActiveSessions(final OperatorId operatorId) {
        return operatorSessionRepository.countActiveRefreshTokens(operatorId, Instant.now(clock));
    }

    @Override
    public void revokeSessions(final OperatorId operatorId) {
        final Instant revokedAt = Instant.now(clock);
        operatorSessionRepository.markSessionsRevoked(operatorId, revokedAt);
        final int deletedTokens = operatorSessionRepository.deleteRefreshTokens(operatorId);
        log.info("Revoked sessions of operator {}: {} refresh tokens removed", operatorId.value(), deletedTokens);
    }
}
