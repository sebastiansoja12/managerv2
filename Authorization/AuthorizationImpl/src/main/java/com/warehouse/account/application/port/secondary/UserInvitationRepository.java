package com.warehouse.account.application.port.secondary;

import com.warehouse.account.domain.model.UserInvitation;
import com.warehouse.commonassets.identificator.SupplierId;

import java.util.Optional;

public interface UserInvitationRepository {
    Optional<UserInvitation> findBySupplierId(final SupplierId supplierId);
    Optional<UserInvitation> findByTokenHash(final String tokenHash);
    void save(final UserInvitation invitation);
}
