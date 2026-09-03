package com.warehouse.auth;

import com.warehouse.auth.infrastructure.dto.PlatformActorDto;

import java.util.Optional;

public interface PlatformActorService {
    Optional<PlatformActorDto> currentActor();
}
