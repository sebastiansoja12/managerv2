package com.warehouse.eventoutbox.consumption;

import static com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.CREATED_TOPIC;
import static com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.EVENTS_TOPIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.SubscriptionCases;
import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.TestListeners;
import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.TestShipmentCreated;
import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.TestShipmentDestinationChanged;
import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.TestShipmentStatusChanged;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;

class IntegrationEventSubscriptionTest {

    private final IntegrationEventRegistry registry = IntegrationEventConsumptionFixture.registry();

    @Test
    void shouldSubscribeConcreteEventToItsOwnTopic() {
        final IntegrationEventSubscription subscription = subscription("onCreated");

        assertThat(subscription.group()).isEqualTo("test-delivery");
        assertThat(subscription.topics()).containsExactly(CREATED_TOPIC);
        assertThat(subscription.types()).containsExactly("test.shipment.created");
    }

    @Test
    void shouldSubscribeBaseEventToTopicsOfAllSubclasses() {
        final IntegrationEventSubscription subscription = subscription("onAnyChange");

        assertThat(subscription.topics()).containsExactlyInAnyOrder(CREATED_TOPIC, EVENTS_TOPIC);
        assertThat(subscription.types()).containsExactlyInAnyOrder("test.shipment.changed", "test.shipment.created",
                "test.shipment.status.changed", "test.shipment.destination.changed");
    }

    @Test
    void shouldNarrowBaseEventToDeclaredTopic() {
        final Method method = Arrays.stream(SubscriptionCases.class.getMethods())
                .filter(candidate -> candidate.getName().equals("onChanged"))
                .findFirst()
                .orElseThrow();
        final IntegrationEventSubscription subscription = IntegrationEventSubscription.from(this.registry, method,
                method.getAnnotation(IntegrationEventListener.class));

        assertThat(subscription.topics()).containsExactly(EVENTS_TOPIC);
        assertThat(subscription.types()).doesNotContain("test.shipment.created");
        assertThat(subscription.accepts(this.registry.findByType("test.shipment.status.changed", 1))).isTrue();
    }

    @Test
    void shouldAcceptOnlyDeclaredTypesWhenTypesAreExplicit() {
        final IntegrationEventSubscription subscription = subscription("onStatusChanged");

        assertThat(subscription.accepts(this.registry.findByType("test.shipment.status.changed", 1))).isTrue();
        assertThat(subscription.accepts(this.registry.findByType("test.shipment.destination.changed", 1))).isFalse();
    }

    @Test
    void shouldRejectEventsOfOtherTypesAndAcceptRecordsWithoutKnownType() {
        final IntegrationEventSubscription subscription = subscription("onCreated");

        assertThat(subscription.accepts(this.registry.findByType("test.shipment.created", 1))).isTrue();
        assertThat(subscription.accepts(this.registry.findByType("test.shipment.status.changed", 1))).isFalse();
        assertThat(subscription.accepts(Optional.empty())).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenNoRegisteredEventMatchesParameter() {
        final IntegrationEventRegistry emptyRegistry = new IntegrationEventRegistry();
        final Method method = method("onCreated");

        assertThatThrownBy(() -> IntegrationEventSubscription.from(emptyRegistry, method,
                method.getAnnotation(IntegrationEventListener.class)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(TestShipmentCreated.class.getName());
    }

    @Test
    void shouldKeepSubclassesOfDifferentTopicsApart() {
        assertThat(this.registry.definitionOf(TestShipmentStatusChanged.class).topic()).isEqualTo(EVENTS_TOPIC);
        assertThat(this.registry.definitionOf(TestShipmentDestinationChanged.class).topic()).isEqualTo(EVENTS_TOPIC);
    }

    private IntegrationEventSubscription subscription(final String methodName) {
        final Method method = method(methodName);
        return IntegrationEventSubscription.from(this.registry, method,
                method.getAnnotation(IntegrationEventListener.class));
    }

    private Method method(final String methodName) {
        return Arrays.stream(TestListeners.class.getMethods())
                .filter(method -> method.getName().equals(methodName))
                .findFirst()
                .orElseThrow();
    }
}
