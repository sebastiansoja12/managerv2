package com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.subscription.application.port.secondary.OperatorSubscriptionRepository;
import com.warehouse.organisationstructure.subscription.domain.model.OperatorSubscription;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary.entity.OperatorSubscriptionEntity;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary.mapper.OperatorSubscriptionEntityMapper;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class OperatorSubscriptionRepositoryImpl implements OperatorSubscriptionRepository {

    private static final Duration CACHE_TTL = Duration.ofSeconds(5);

    private static final int MAX_CACHE_SIZE = 10_000;

    private final OperatorSubscriptionReadRepository repository;

    private final Clock clock;

    private final Map<OperatorId, CachedOperatorSubscription> cache = new ConcurrentHashMap<>();

    public OperatorSubscriptionRepositoryImpl(final OperatorSubscriptionReadRepository repository, final Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public Optional<OperatorSubscription> find(final OperatorId operatorId) {
        final Instant now = Instant.now(clock);
        final CachedOperatorSubscription cached = cache.get(operatorId);
        if (cached != null && cached.expiresAt().isAfter(now)) {
            return cached.entity().map(OperatorSubscriptionEntityMapper::toModel);
        }
        final Optional<OperatorSubscriptionEntity> entity = repository.findByOperatorId(operatorId);
        cache(operatorId, entity, now);
        return entity.map(OperatorSubscriptionEntityMapper::toModel);
    }

    @Override
    public OperatorSubscription save(final OperatorSubscription operatorSubscription) {
        final OperatorSubscriptionEntity saved = repository.save(
                OperatorSubscriptionEntityMapper.toEntity(operatorSubscription));
        cache(saved.operatorId(), Optional.of(saved), Instant.now(clock));
        return OperatorSubscriptionEntityMapper.toModel(saved);
    }

    private void cache(final OperatorId operatorId,
                       final Optional<OperatorSubscriptionEntity> entity,
                       final Instant now) {
        if (cache.size() >= MAX_CACHE_SIZE) {
            cache.values().removeIf(cached -> !cached.expiresAt().isAfter(now));
        }
        if (cache.size() >= MAX_CACHE_SIZE) {
            cache.clear();
        }
        cache.put(operatorId, new CachedOperatorSubscription(entity, now.plus(CACHE_TTL)));
    }

    private record CachedOperatorSubscription(Optional<OperatorSubscriptionEntity> entity, Instant expiresAt) {
    }
}
