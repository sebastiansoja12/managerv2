package com.warehouse.auth.domain.port.secondary;

import com.warehouse.commonassets.identificator.PlatformOwnerId;

import java.util.Optional;

public interface PlatformOwnerRefreshTokenRepository {

    String create(final PlatformOwnerId platformOwnerId);

    Optional<PlatformOwnerId> findActivePlatformOwner(final String refreshToken);

    void delete(final String refreshToken);

    void deleteAll(final PlatformOwnerId platformOwnerId);
}
