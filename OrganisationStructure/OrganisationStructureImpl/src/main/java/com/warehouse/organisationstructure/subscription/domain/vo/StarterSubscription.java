package com.warehouse.organisationstructure.subscription.domain.vo;

import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionPlan;
import com.warehouse.organisationstructure.subscription.domain.exception.InvalidSubscriptionException;

public record StarterSubscription(SubscriptionPlan plan, int durationDays) {

    public StarterSubscription {
        if (plan == null) {
            throw new InvalidSubscriptionException("Starter subscription plan is required");
        }
        if (durationDays < 1) {
            throw new InvalidSubscriptionException("Starter subscription must last at least one day");
        }
    }
}
