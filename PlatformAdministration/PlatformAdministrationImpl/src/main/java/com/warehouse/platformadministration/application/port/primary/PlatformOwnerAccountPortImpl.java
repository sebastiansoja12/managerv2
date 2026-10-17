package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.platformadministration.application.port.primary.command.CreatePlatformOwnerAccountCommand;
import com.warehouse.platformadministration.application.port.secondary.PlatformActorServicePort;
import com.warehouse.platformadministration.application.port.secondary.PlatformOwnerAccountServicePort;
import com.warehouse.platformadministration.domain.exception.PlatformActorRequiredException;
import com.warehouse.platformadministration.domain.vo.PlatformActor;
import com.warehouse.platformadministration.domain.vo.PlatformOwnerAccount;

import java.util.List;

public class PlatformOwnerAccountPortImpl implements PlatformOwnerAccountPort {

    private final PlatformOwnerAccountServicePort platformOwnerAccountServicePort;

    private final PlatformActorServicePort platformActorServicePort;

    public PlatformOwnerAccountPortImpl(final PlatformOwnerAccountServicePort platformOwnerAccountServicePort,
                                        final PlatformActorServicePort platformActorServicePort) {
        this.platformOwnerAccountServicePort = platformOwnerAccountServicePort;
        this.platformActorServicePort = platformActorServicePort;
    }

    @Override
    public List<PlatformOwnerAccount> findAll() {
        return platformOwnerAccountServicePort.findAll();
    }

    @Override
    public PlatformOwnerId create(final CreatePlatformOwnerAccountCommand command) {
        return platformOwnerAccountServicePort.create(command);
    }

    @Override
    public void block(final PlatformOwnerId platformOwnerId) {
        final PlatformActor actor = platformActorServicePort.currentActor()
                .filter(currentActor -> !currentActor.impersonation())
                .orElseThrow(PlatformActorRequiredException::new);
        platformOwnerAccountServicePort.block(platformOwnerId, actor.platformOwnerId());
    }

    @Override
    public void unblock(final PlatformOwnerId platformOwnerId) {
        platformOwnerAccountServicePort.unblock(platformOwnerId);
    }

    @Override
    public void changePassword(final PlatformOwnerId platformOwnerId, final String password) {
        platformOwnerAccountServicePort.changePassword(platformOwnerId, password);
    }
}
