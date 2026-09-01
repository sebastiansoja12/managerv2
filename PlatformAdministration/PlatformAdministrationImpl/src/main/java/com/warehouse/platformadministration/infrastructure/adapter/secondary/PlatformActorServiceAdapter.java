package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.auth.PlatformActorService;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.secondary.PlatformActorServicePort;
import com.warehouse.platformadministration.domain.vo.PlatformActor;

import java.util.Optional;

public class PlatformActorServiceAdapter implements PlatformActorServicePort {

    private final PlatformActorService platformActorService;

    public PlatformActorServiceAdapter(final PlatformActorService platformActorService) {
        this.platformActorService = platformActorService;
    }

    @Override
    public Optional<PlatformActor> currentActor() {
        return platformActorService.currentActor()
                .map(actor -> new PlatformActor(
                        new PlatformOwnerId(actor.platformOwnerId()),
                        actor.username(),
                        actor.impersonation(),
                        actor.impersonatedUserId() != null ? new UserId(actor.impersonatedUserId()) : null,
                        actor.operatorId() != null ? OperatorId.of(actor.operatorId()) : null));
    }
}
