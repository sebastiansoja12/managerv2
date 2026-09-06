package com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary;

import com.warehouse.auth.PlatformActorService;
import com.warehouse.organisationstructure.subscription.application.port.secondary.PlatformActorServicePort;
import com.warehouse.organisationstructure.subscription.domain.vo.PlatformActor;

import java.util.Optional;

public class PlatformActorServiceAdapter implements PlatformActorServicePort {

    private final PlatformActorService platformActorService;

    public PlatformActorServiceAdapter(final PlatformActorService platformActorService) {
        this.platformActorService = platformActorService;
    }

    @Override
    public Optional<PlatformActor> currentActor() {
        return platformActorService.currentActor()
                .map(actor -> new PlatformActor(actor.username(), actor.impersonation()));
    }

    @Override
    public boolean isPlatformActorRequest() {
        return platformActorService.isPlatformActorRequest();
    }
}
