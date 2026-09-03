package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.platformadministration.application.port.secondary.MaintenanceModeRepository;
import com.warehouse.platformadministration.domain.model.MaintenanceMode;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.entity.PlatformMaintenanceEntity;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class MaintenanceModeRepositoryImpl implements MaintenanceModeRepository {

    private static final Duration CACHE_TTL = Duration.ofSeconds(5);

    private final PlatformMaintenanceReadRepository repository;

    private final Clock clock;

    private volatile CachedMaintenanceMode cached;

    public MaintenanceModeRepositoryImpl(final PlatformMaintenanceReadRepository repository, final Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public MaintenanceMode current() {
        final Instant now = Instant.now(clock);
        final CachedMaintenanceMode currentCache = cached;
        if (currentCache != null && currentCache.expiresAt().isAfter(now)) {
            return currentCache.maintenanceMode();
        }
        final MaintenanceMode maintenanceMode = repository.findById(PlatformMaintenanceEntity.SINGLETON_ID)
                .map(this::toModel)
                .orElseGet(MaintenanceMode::disabled);
        cached = new CachedMaintenanceMode(maintenanceMode, now.plus(CACHE_TTL));
        return maintenanceMode;
    }

    @Override
    public MaintenanceMode save(final MaintenanceMode maintenanceMode) {
        final MaintenanceMode saved = toModel(repository.save(new PlatformMaintenanceEntity(
                PlatformMaintenanceEntity.SINGLETON_ID, maintenanceMode.enabled(), maintenanceMode.message(),
                maintenanceMode.updatedBy(), maintenanceMode.updatedAt())));
        cached = new CachedMaintenanceMode(saved, Instant.now(clock).plus(CACHE_TTL));
        return saved;
    }

    private MaintenanceMode toModel(final PlatformMaintenanceEntity entity) {
        return new MaintenanceMode(entity.isEnabled(), entity.getMessage(), entity.getUpdatedBy(), entity.getUpdatedAt());
    }

    private record CachedMaintenanceMode(MaintenanceMode maintenanceMode, Instant expiresAt) {
    }
}
