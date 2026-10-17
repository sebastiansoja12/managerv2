package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.CreatePlatformOwnerCommand;
import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.commonassets.identificator.PlatformOwnerId;

import java.util.List;

public interface PlatformOwnerManagementPort {

    List<PlatformOwner> findAll();

    PlatformOwnerId create(final CreatePlatformOwnerCommand command);

    void block(final PlatformOwnerId platformOwnerId, final PlatformOwnerId actingPlatformOwnerId);

    void unblock(final PlatformOwnerId platformOwnerId);

    void changePassword(final PlatformOwnerId platformOwnerId, final String password);
}
