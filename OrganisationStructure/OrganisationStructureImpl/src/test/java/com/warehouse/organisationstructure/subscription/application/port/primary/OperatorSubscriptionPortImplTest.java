package com.warehouse.organisationstructure.subscription.application.port.primary;

import com.warehouse.organisationstructure.subscription.InMemoryOperatorSubscriptionStore;
import com.warehouse.organisationstructure.subscription.application.port.primary.command.AssignStarterSubscriptionCommand;
import com.warehouse.organisationstructure.subscription.application.port.primary.command.AssignSubscriptionCommand;
import com.warehouse.organisationstructure.subscription.application.port.primary.result.OperatorSubscriptionResult;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionPlan;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionStatus;
import com.warehouse.organisationstructure.subscription.domain.exception.OperatorNotFoundException;
import com.warehouse.organisationstructure.subscription.domain.exception.PlatformOwnerRequiredException;
import com.warehouse.organisationstructure.subscription.domain.model.OperatorSubscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.IMPERSONATING_PLATFORM_OWNER;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.NOW;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.OPERATOR_ID;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.PLATFORM_OWNER;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.STARTER_SUBSCRIPTION;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.TODAY;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.UNKNOWN_OPERATOR_ID;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.clockAt;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.subscription;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OperatorSubscriptionPortImplTest {

    private InMemoryOperatorSubscriptionStore store;

    private OperatorSubscriptionPortImpl operatorSubscriptionPort;

    @BeforeEach
    void setUp() {
        store = new InMemoryOperatorSubscriptionStore(Set.of(OPERATOR_ID));
        operatorSubscriptionPort = portAt(NOW);
    }

    @Test
    void shouldAssignPlanAndRecordPlatformOwner() {
        store.actAsPlatformActor(PLATFORM_OWNER);

        final OperatorSubscriptionResult result = operatorSubscriptionPort.assign(new AssignSubscriptionCommand(
                OPERATOR_ID, SubscriptionPlan.BASIC, TODAY, TODAY.plusMonths(1)));

        assertThat(result.plan()).isEqualTo(SubscriptionPlan.BASIC);
        assertThat(result.status()).isEqualTo(SubscriptionStatus.ACTIVE);
        assertThat(result.updatedBy()).isEqualTo("owner");
        assertThat(result.updatedAt()).isEqualTo(NOW);
        assertThat(store.find(OPERATOR_ID)).isPresent();
    }

    @Test
    void shouldChangePlanAndPeriodOfExistingSubscription() {
        store.actAsPlatformActor(PLATFORM_OWNER);
        operatorSubscriptionPort.assign(new AssignSubscriptionCommand(OPERATOR_ID, SubscriptionPlan.MINI,
                TODAY.minusMonths(2), TODAY.minusDays(1)));

        final OperatorSubscriptionResult result = operatorSubscriptionPort.assign(new AssignSubscriptionCommand(
                OPERATOR_ID, SubscriptionPlan.ENTERPRISE, TODAY, TODAY.plusYears(1)));

        assertThat(result.plan()).isEqualTo(SubscriptionPlan.ENTERPRISE);
        assertThat(result.endDate()).isEqualTo(TODAY.plusYears(1));
        assertThat(operatorSubscriptionPort.find(OPERATOR_ID))
                .map(OperatorSubscriptionResult::status)
                .contains(SubscriptionStatus.ACTIVE);
    }

    @Test
    void shouldRejectAssignmentWithoutPlatformOwner() {
        assertThrows(PlatformOwnerRequiredException.class, () -> operatorSubscriptionPort.assign(
                new AssignSubscriptionCommand(OPERATOR_ID, SubscriptionPlan.BASIC, TODAY, TODAY)));
        assertThat(store.find(OPERATOR_ID)).isEmpty();
    }

    @Test
    void shouldRejectAssignmentDuringImpersonation() {
        store.actAsPlatformActor(IMPERSONATING_PLATFORM_OWNER);

        assertThrows(PlatformOwnerRequiredException.class, () -> operatorSubscriptionPort.assign(
                new AssignSubscriptionCommand(OPERATOR_ID, SubscriptionPlan.BASIC, TODAY, TODAY)));
        assertThat(store.find(OPERATOR_ID)).isEmpty();
    }

    @Test
    void shouldRejectAssignmentForUnknownOperator() {
        store.actAsPlatformActor(PLATFORM_OWNER);

        assertThrows(OperatorNotFoundException.class, () -> operatorSubscriptionPort.assign(
                new AssignSubscriptionCommand(UNKNOWN_OPERATOR_ID, SubscriptionPlan.BASIC, TODAY, TODAY)));
        assertThrows(OperatorNotFoundException.class, () -> operatorSubscriptionPort.find(UNKNOWN_OPERATOR_ID));
    }

    @Test
    void shouldDenyAccessWhenOperatorHasNoSubscription() {
        store.actAsOperator(OPERATOR_ID);

        assertThat(operatorSubscriptionPort.isCurrentAccessAllowed()).isFalse();
        assertThat(operatorSubscriptionPort.findCurrent()).isEmpty();
    }

    @Test
    void shouldAllowAccessOnLastDayOfSubscription() {
        store.save(subscription(SubscriptionPlan.BASIC, TODAY.minusMonths(1), TODAY));
        store.actAsOperator(OPERATOR_ID);

        assertThat(operatorSubscriptionPort.isCurrentAccessAllowed()).isTrue();
    }

    @Test
    void shouldDenyAccessWhenSubscriptionExpired() {
        store.save(subscription(SubscriptionPlan.BASIC, TODAY.minusMonths(1), TODAY.minusDays(1)));
        store.actAsOperator(OPERATOR_ID);

        assertThat(operatorSubscriptionPort.isCurrentAccessAllowed()).isFalse();
        assertThat(operatorSubscriptionPort.findCurrent())
                .map(OperatorSubscriptionResult::status)
                .contains(SubscriptionStatus.EXPIRED);
    }

    @Test
    void shouldDenyAccessWhenSubscriptionHasNotStarted() {
        store.save(subscription(SubscriptionPlan.MINI, TODAY.plusDays(1), TODAY.plusMonths(1)));
        store.actAsOperator(OPERATOR_ID);

        assertThat(operatorSubscriptionPort.isCurrentAccessAllowed()).isFalse();
        assertThat(operatorSubscriptionPort.findCurrent())
                .map(OperatorSubscriptionResult::status)
                .contains(SubscriptionStatus.NOT_STARTED);
    }

    @Test
    void shouldEvaluateDateBoundaryInBusinessTimeZone() {
        final LocalDate endDate = LocalDate.of(2026, 10, 31);
        store.save(subscription(SubscriptionPlan.BASIC, endDate.minusMonths(1), endDate));
        store.actAsOperator(OPERATOR_ID);

        final boolean beforeLocalMidnight = portAt(Instant.parse("2026-10-31T22:59:00Z")).isCurrentAccessAllowed();
        final boolean afterLocalMidnight = portAt(Instant.parse("2026-10-31T23:01:00Z")).isCurrentAccessAllowed();

        assertThat(beforeLocalMidnight).isTrue();
        assertThat(afterLocalMidnight).isFalse();
    }

    @Test
    void shouldAllowPlatformActorRegardlessOfSubscription() {
        store.save(subscription(SubscriptionPlan.BASIC, TODAY.minusMonths(1), TODAY.minusDays(1)));
        store.actAsOperator(OPERATOR_ID);
        store.actAsPlatformActor(IMPERSONATING_PLATFORM_OWNER);

        assertThat(operatorSubscriptionPort.isCurrentAccessAllowed()).isTrue();
    }

    @Test
    void shouldAllowRequestsWithoutOperatorContext() {
        assertThat(operatorSubscriptionPort.isCurrentAccessAllowed()).isTrue();
    }

    @Test
    void shouldAssignStarterPlanFromOperatorCreationDayInBusinessTimeZone() {
        operatorSubscriptionPort.assignStarter(new AssignStarterSubscriptionCommand(OPERATOR_ID,
                Instant.parse("2026-10-18T22:30:00Z")));

        assertThat(store.find(OPERATOR_ID)).hasValueSatisfying(subscription -> {
            assertThat(subscription.getPlan()).isEqualTo(SubscriptionPlan.MINI);
            assertThat(subscription.getStartDate()).isEqualTo(TODAY);
            assertThat(subscription.getEndDate()).isEqualTo(TODAY.plusDays(29));
            assertThat(subscription.getUpdatedBy()).isEqualTo(OperatorSubscription.STARTER_UPDATED_BY);
        });
    }

    @Test
    void shouldKeepExistingPlanWhenStarterPlanArrivesLater() {
        store.save(subscription(SubscriptionPlan.ENTERPRISE, TODAY, TODAY.plusYears(1)));

        operatorSubscriptionPort.assignStarter(new AssignStarterSubscriptionCommand(OPERATOR_ID, NOW));

        assertThat(store.find(OPERATOR_ID))
                .map(OperatorSubscription::getPlan)
                .contains(SubscriptionPlan.ENTERPRISE);
    }

    private OperatorSubscriptionPortImpl portAt(final Instant instant) {
        return new OperatorSubscriptionPortImpl(store, store, store, store, STARTER_SUBSCRIPTION, clockAt(instant));
    }
}
