package com.warehouse.organisationstructure.subscription.domain.model;

import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionPlan;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionStatus;
import com.warehouse.organisationstructure.subscription.domain.exception.InvalidSubscriptionException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.NOW;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.OPERATOR_ID;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.subscription;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OperatorSubscriptionTest {

    private static final LocalDate START = LocalDate.of(2026, 10, 1);

    private static final LocalDate END = LocalDate.of(2026, 10, 31);

    private final OperatorSubscription october = subscription(SubscriptionPlan.BASIC, START, END);

    @Test
    void shouldNotBeStartedBeforeStartDate() {
        assertThat(october.statusOn(START.minusDays(1))).isEqualTo(SubscriptionStatus.NOT_STARTED);
        assertThat(october.allowsAccessOn(START.minusDays(1))).isFalse();
    }

    @Test
    void shouldBeActiveFromStartDateUntilEndDateInclusive() {
        assertThat(october.statusOn(START)).isEqualTo(SubscriptionStatus.ACTIVE);
        assertThat(october.statusOn(LocalDate.of(2026, 10, 15))).isEqualTo(SubscriptionStatus.ACTIVE);
        assertThat(october.statusOn(END)).isEqualTo(SubscriptionStatus.ACTIVE);
        assertThat(october.allowsAccessOn(END)).isTrue();
    }

    @Test
    void shouldExpireTheDayAfterEndDate() {
        assertThat(october.statusOn(END.plusDays(1))).isEqualTo(SubscriptionStatus.EXPIRED);
        assertThat(october.allowsAccessOn(END.plusDays(1))).isFalse();
    }

    @Test
    void shouldAllowSingleDaySubscription() {
        final OperatorSubscription singleDay = subscription(SubscriptionPlan.MINI, START, START);

        assertThat(singleDay.statusOn(START)).isEqualTo(SubscriptionStatus.ACTIVE);
        assertThat(singleDay.statusOn(START.plusDays(1))).isEqualTo(SubscriptionStatus.EXPIRED);
    }

    @Test
    void shouldChangePlanAndPeriod() {
        final OperatorSubscription operatorSubscription = subscription(SubscriptionPlan.MINI, START, END);
        final Instant changedAt = NOW.plusSeconds(60);

        operatorSubscription.changePlan(SubscriptionPlan.ENTERPRISE, END.plusDays(1), END.plusYears(1), "support",
                changedAt);

        assertThat(operatorSubscription.getPlan()).isEqualTo(SubscriptionPlan.ENTERPRISE);
        assertThat(operatorSubscription.getStartDate()).isEqualTo(END.plusDays(1));
        assertThat(operatorSubscription.getEndDate()).isEqualTo(END.plusYears(1));
        assertThat(operatorSubscription.getUpdatedBy()).isEqualTo("support");
        assertThat(operatorSubscription.getUpdatedAt()).isEqualTo(changedAt);
        assertThat(operatorSubscription.getOperatorId()).isEqualTo(OPERATOR_ID);
    }

    @Test
    void shouldKeepStateWhenChangedPeriodIsInvalid() {
        final OperatorSubscription operatorSubscription = subscription(SubscriptionPlan.BASIC, START, END);

        assertThrows(InvalidSubscriptionException.class,
                () -> operatorSubscription.changePlan(SubscriptionPlan.MINI, END, START, "support", NOW));

        assertThat(operatorSubscription.getPlan()).isEqualTo(SubscriptionPlan.BASIC);
        assertThat(operatorSubscription.getStartDate()).isEqualTo(START);
        assertThat(operatorSubscription.getEndDate()).isEqualTo(END);
    }

    @Test
    void shouldRejectEndDateBeforeStartDate() {
        assertThrows(InvalidSubscriptionException.class,
                () -> OperatorSubscription.assign(OPERATOR_ID, SubscriptionPlan.ENTERPRISE, END, START, "owner", NOW));
    }

    @Test
    void shouldRejectMissingPlan() {
        assertThrows(InvalidSubscriptionException.class,
                () -> OperatorSubscription.assign(OPERATOR_ID, null, START, END, "owner", NOW));
    }

    @Test
    void shouldRejectMissingDates() {
        assertThrows(InvalidSubscriptionException.class,
                () -> OperatorSubscription.assign(OPERATOR_ID, SubscriptionPlan.BASIC, null, END, "owner", NOW));
        assertThrows(InvalidSubscriptionException.class,
                () -> OperatorSubscription.assign(OPERATOR_ID, SubscriptionPlan.BASIC, START, null, "owner", NOW));
    }
}
