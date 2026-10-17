package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.platformadministration.application.port.primary.command.ChangeMaintenanceModeCommand;
import com.warehouse.platformadministration.application.port.secondary.MaintenanceModeRepository;
import com.warehouse.platformadministration.application.port.secondary.PlatformActorServicePort;
import com.warehouse.platformadministration.domain.exception.PlatformActorRequiredException;
import com.warehouse.platformadministration.domain.model.MaintenanceMode;
import com.warehouse.platformadministration.domain.vo.PlatformActor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;

@Slf4j
public class MaintenanceModePortImpl implements MaintenanceModePort {

    private final MaintenanceModeRepository maintenanceModeRepository;

    private final PlatformActorServicePort platformActorServicePort;

    private final Clock clock;

    public MaintenanceModePortImpl(final MaintenanceModeRepository maintenanceModeRepository,
                                   final PlatformActorServicePort platformActorServicePort,
                                   final Clock clock) {
        this.maintenanceModeRepository = maintenanceModeRepository;
        this.platformActorServicePort = platformActorServicePort;
        this.clock = clock;
    }

    @Override
    public MaintenanceMode current() {
        return maintenanceModeRepository.current();
    }

    @Override
    public MaintenanceMode change(final ChangeMaintenanceModeCommand command) {
        final PlatformActor actor = platformActorServicePort.currentActor()
                .filter(currentActor -> !currentActor.impersonation())
                .orElseThrow(PlatformActorRequiredException::new);
        final MaintenanceMode maintenanceMode = maintenanceModeRepository.save(MaintenanceMode.change(
                command.enabled(), command.message(), actor.username(), Instant.now(clock)));
        log.warn("Maintenance mode {} by platform owner {}", maintenanceMode.enabled() ? "enabled" : "disabled",
                actor.username());
        return maintenanceMode;
    }

    @Override
    public boolean isBlocked(final String path, final String httpMethod) {
        final MaintenanceMode maintenanceMode = maintenanceModeRepository.current();
        if (!maintenanceMode.enabled()) {
            return false;
        }
        return maintenanceMode.blocks(path, httpMethod, platformActorServicePort.currentActor().isPresent());
    }
}
