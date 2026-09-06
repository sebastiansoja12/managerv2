package com.warehouse.organisationstructure.subscription;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionPlan;
import com.warehouse.organisationstructure.subscription.domain.model.OperatorSubscription;
import com.warehouse.organisationstructure.subscription.domain.vo.PlatformActor;
import com.warehouse.organisationstructure.subscription.domain.vo.StarterSubscription;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public final class OperatorSubscriptionFixture {

    public static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    public static final OperatorId UNKNOWN_OPERATOR_ID = OperatorId.of(404L);

    public static final ZoneId BUSINESS_ZONE = ZoneId.of("Europe/Warsaw");

    public static final Instant NOW = Instant.parse("2026-10-19T10:00:00Z");

    public static final LocalDate TODAY = LocalDate.of(2026, 10, 19);

    public static final PlatformActor PLATFORM_OWNER = new PlatformActor("owner", false);

    public static final PlatformActor IMPERSONATING_PLATFORM_OWNER = new PlatformActor("owner", true);

    public static final StarterSubscription STARTER_SUBSCRIPTION = new StarterSubscription(SubscriptionPlan.MINI, 30);

    private OperatorSubscriptionFixture() {
    }

    public static Clock clockAt(final Instant instant) {
        return Clock.fixed(instant, BUSINESS_ZONE);
    }

    public static OperatorSubscription subscription(final SubscriptionPlan plan,
                                                    final LocalDate startDate,
                                                    final LocalDate endDate) {
        return OperatorSubscription.assign(OPERATOR_ID, plan, startDate, endDate, PLATFORM_OWNER.username(), NOW);
    }
}
