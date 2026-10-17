package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.platformadministration.application.port.primary.command.CreatePlatformOwnerAccountCommand;
import com.warehouse.platformadministration.domain.vo.PlatformOwnerAccount;

import java.util.List;

public interface PlatformOwnerAccountServicePort {

    List<PlatformOwnerAccount> findAll();

    PlatformOwnerId create(final CreatePlatformOwnerAccountCommand command);

    void block(final PlatformOwnerId platformOwnerId, final PlatformOwnerId actingPlatformOwnerId);

    void unblock(final PlatformOwnerId platformOwnerId);

    void changePassword(final PlatformOwnerId platformOwnerId, final String password);
}
