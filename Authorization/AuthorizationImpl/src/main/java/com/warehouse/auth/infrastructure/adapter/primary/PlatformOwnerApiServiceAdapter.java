package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.PlatformOwnerApiService;
import com.warehouse.auth.domain.model.CreatePlatformOwnerCommand;
import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.port.primary.PlatformOwnerManagementPort;
import com.warehouse.auth.infrastructure.dto.CreatePlatformOwnerApiRequest;
import com.warehouse.auth.infrastructure.dto.PlatformOwnerDto;
import com.warehouse.auth.infrastructure.dto.PlatformOwnerIdDto;
import com.warehouse.commonassets.identificator.PlatformOwnerId;

import java.util.List;

public class PlatformOwnerApiServiceAdapter implements PlatformOwnerApiService {

    private final PlatformOwnerManagementPort platformOwnerManagementPort;

    public PlatformOwnerApiServiceAdapter(final PlatformOwnerManagementPort platformOwnerManagementPort) {
        this.platformOwnerManagementPort = platformOwnerManagementPort;
    }

    @Override
    public List<PlatformOwnerDto> findAll() {
        return platformOwnerManagementPort.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public PlatformOwnerIdDto create(final CreatePlatformOwnerApiRequest request) {
        final PlatformOwnerId platformOwnerId = platformOwnerManagementPort.create(new CreatePlatformOwnerCommand(
                request.username(), request.password(), request.firstName(), request.lastName(), request.email()));
        return new PlatformOwnerIdDto(platformOwnerId.value());
    }

    @Override
    public void block(final PlatformOwnerId platformOwnerId, final PlatformOwnerId actingPlatformOwnerId) {
        platformOwnerManagementPort.block(platformOwnerId, actingPlatformOwnerId);
    }

    @Override
    public void unblock(final PlatformOwnerId platformOwnerId) {
        platformOwnerManagementPort.unblock(platformOwnerId);
    }

    @Override
    public void changePassword(final PlatformOwnerId platformOwnerId, final String password) {
        platformOwnerManagementPort.changePassword(platformOwnerId, password);
    }

    private PlatformOwnerDto map(final PlatformOwner platformOwner) {
        return new PlatformOwnerDto(platformOwner.getPlatformOwnerId().value(), platformOwner.getUsername(),
                platformOwner.getFirstName(), platformOwner.getLastName(), platformOwner.getEmail(),
                platformOwner.isActive(), platformOwner.createdAt());
    }
}
