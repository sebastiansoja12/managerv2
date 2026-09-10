package com.warehouse.organisationstructure.subscription.application.port.secondary;

import com.warehouse.organisationstructure.subscription.domain.vo.PlatformActor;

import java.util.Optional;

public interface PlatformActorServicePort {

    Optional<PlatformActor> currentActor();

    boolean isPlatformActorRequest();
}
