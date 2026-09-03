package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.platformadministration.application.port.primary.command.CreatePlatformOwnerAccountCommand;
import com.warehouse.platformadministration.domain.vo.PlatformOwnerAccount;

import java.util.List;

public interface PlatformOwnerAccountPort {

    List<PlatformOwnerAccount> findAll();

    PlatformOwnerId create(final CreatePlatformOwnerAccountCommand command);

    void block(final PlatformOwnerId platformOwnerId);

    void unblock(final PlatformOwnerId platformOwnerId);

    void changePassword(final PlatformOwnerId platformOwnerId, final String password);
}
