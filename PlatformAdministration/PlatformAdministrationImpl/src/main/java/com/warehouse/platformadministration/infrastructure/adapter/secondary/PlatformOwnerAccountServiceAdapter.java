package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.auth.PlatformOwnerApiService;
import com.warehouse.auth.infrastructure.dto.CreatePlatformOwnerApiRequest;
import com.warehouse.auth.infrastructure.dto.PlatformOwnerIdDto;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.platformadministration.application.port.primary.command.CreatePlatformOwnerAccountCommand;
import com.warehouse.platformadministration.application.port.secondary.PlatformOwnerAccountServicePort;
import com.warehouse.platformadministration.domain.vo.PlatformOwnerAccount;

import java.util.List;

public class PlatformOwnerAccountServiceAdapter implements PlatformOwnerAccountServicePort {

    private final PlatformOwnerApiService platformOwnerApiService;

    public PlatformOwnerAccountServiceAdapter(final PlatformOwnerApiService platformOwnerApiService) {
        this.platformOwnerApiService = platformOwnerApiService;
    }

    @Override
    public List<PlatformOwnerAccount> findAll() {
        return platformOwnerApiService.findAll()
                .stream()
                .map(owner -> new PlatformOwnerAccount(new PlatformOwnerId(owner.platformOwnerId()), owner.username(),
                        owner.firstName(), owner.lastName(), owner.email(), owner.active(), owner.createdAt()))
                .toList();
    }

    @Override
    public PlatformOwnerId create(final CreatePlatformOwnerAccountCommand command) {
        final PlatformOwnerIdDto platformOwnerId = platformOwnerApiService.create(new CreatePlatformOwnerApiRequest(
                command.username(), command.password(), command.firstName(), command.lastName(), command.email()));
        return new PlatformOwnerId(platformOwnerId.value());
    }

    @Override
    public void block(final PlatformOwnerId platformOwnerId, final PlatformOwnerId actingPlatformOwnerId) {
        platformOwnerApiService.block(platformOwnerId, actingPlatformOwnerId);
    }

    @Override
    public void unblock(final PlatformOwnerId platformOwnerId) {
        platformOwnerApiService.unblock(platformOwnerId);
    }

    @Override
    public void changePassword(final PlatformOwnerId platformOwnerId, final String password) {
        platformOwnerApiService.changePassword(platformOwnerId, password);
    }
}
