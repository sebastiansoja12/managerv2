package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.port.secondary.OperatorStatusServicePort;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.OperatorApiService;
import org.springframework.beans.factory.ObjectProvider;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OperatorStatusServiceAdapter implements OperatorStatusServicePort {

    private static final Duration CACHE_TTL = Duration.ofSeconds(15);

    private final ObjectProvider<OperatorApiService> operatorApiService;

    private final Clock clock;

    private final Map<Long, CachedStatus> statuses = new ConcurrentHashMap<>();

    public OperatorStatusServiceAdapter(final ObjectProvider<OperatorApiService> operatorApiService,
                                        final Clock clock) {
        this.operatorApiService = operatorApiService;
        this.clock = clock;
    }

    @Override
    public boolean isActive(final OperatorId operatorId) {
        final Instant now = Instant.now(clock);
        final CachedStatus cached = statuses.get(operatorId.value());
        if (cached != null && cached.expiresAt().isAfter(now)) {
            return cached.active();
        }
        final boolean active = operatorApiService.getObject().isActive(operatorId);
        statuses.put(operatorId.value(), new CachedStatus(active, now.plus(CACHE_TTL)));
        return active;
    }

    private record CachedStatus(boolean active, Instant expiresAt) {
    }
}
