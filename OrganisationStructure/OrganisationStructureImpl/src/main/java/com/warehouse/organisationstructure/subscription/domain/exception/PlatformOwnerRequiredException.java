package com.warehouse.organisationstructure.subscription.domain.exception;

public class PlatformOwnerRequiredException extends RuntimeException {

    public PlatformOwnerRequiredException() {
        super("Subscription can be managed only by a platform owner outside impersonation");
    }
}
