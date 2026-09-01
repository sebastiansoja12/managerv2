package com.warehouse.platformadministration.domain.exception;

public class PlatformActorRequiredException extends RuntimeException {

    public PlatformActorRequiredException() {
        super("Authenticated platform owner is required");
    }
}
