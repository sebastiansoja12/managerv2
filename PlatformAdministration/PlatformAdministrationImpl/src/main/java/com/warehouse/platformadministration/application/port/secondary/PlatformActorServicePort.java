package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.platformadministration.domain.vo.PlatformActor;

import java.util.Optional;

public interface PlatformActorServicePort {

    Optional<PlatformActor> currentActor();
}
