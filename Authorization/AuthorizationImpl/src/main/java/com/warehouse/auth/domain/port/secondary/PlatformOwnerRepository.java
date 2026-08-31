package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.commonassets.identificator.PlatformOwnerId;

import java.util.List;
import java.util.Optional;

public interface PlatformOwnerRepository {

    Optional<PlatformOwner> findByUsername(final String username);

    Optional<PlatformOwner> findById(final PlatformOwnerId platformOwnerId);

    List<PlatformOwner> findAll();

    void save(final PlatformOwner platformOwner);

    PlatformOwnerId nextPlatformOwnerId();

    boolean existsByUsernameOrEmail(final String username, final String email);

    long countActive();
}
