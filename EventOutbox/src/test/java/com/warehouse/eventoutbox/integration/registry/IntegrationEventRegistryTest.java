package com.warehouse.eventoutbox.integration.registry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;

class IntegrationEventRegistryTest {

    @Test
    void shouldRegisterEventFromAnnotationWithMessageKey() {
        final IntegrationEventRegistry registry = new IntegrationEventRegistry();

        registry.register(TestShipmentCreated.class);

        final IntegrationEventDefinition<?> definition = registry.definitionOf(TestShipmentCreated.class);
        assertThat(definition.type()).isEqualTo("shipment.created");
        assertThat(definition.version()).isEqualTo(1);
        assertThat(definition.topic()).isEqualTo("shipment.created");
        assertThat(definition.messageKey(new TestShipmentCreated("42"))).contains("42");
        assertThat(registry.findByType("shipment.created", 1)).contains(definition);
    }

    @Test
    void shouldReturnEmptyMessageKeyWhenEventDoesNotDefineKey() {
        final IntegrationEventRegistry registry = new IntegrationEventRegistry();

        registry.register(TestAuditLogEntry.class);

        assertThat(registry.definitionOf(TestAuditLogEntry.class).messageKey(new TestAuditLogEntry())).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenEventClassIsRegisteredTwice() {
        final IntegrationEventRegistry registry = new IntegrationEventRegistry();
        registry.register(TestShipmentCreated.class);

        assertThatThrownBy(() -> registry.register(TestShipmentCreated.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(TestShipmentCreated.class.getName());
    }

    @Test
    void shouldThrowExceptionWhenTypeAndVersionAreDeclaredByDifferentClasses() {
        final IntegrationEventRegistry registry = new IntegrationEventRegistry();
        registry.register(TestShipmentCreated.class);

        assertThatThrownBy(() -> registry.register(TestDuplicatedShipmentCreated.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("shipment.created@1");
    }

    @Test
    void shouldAllowSameTypeWithDifferentVersion() {
        final IntegrationEventRegistry registry = new IntegrationEventRegistry();
        registry.register(TestShipmentCreated.class);

        registry.register(TestShipmentCreatedV2.class);

        assertThat(registry.definitions()).hasSize(2);
        assertThat(registry.findByType("shipment.created", 2))
                .map(definition -> definition.eventClass().getName())
                .contains(TestShipmentCreatedV2.class.getName());
    }

    @Test
    void shouldThrowExceptionWhenEventDoesNotDeclareIntegrationEventType() {
        final IntegrationEventRegistry registry = new IntegrationEventRegistry();

        assertThatThrownBy(() -> registry.register(TestUndeclaredEvent.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(TestUndeclaredEvent.class.getName());
    }

    @Test
    void shouldThrowExceptionWhenEventIsNotRegistered() {
        final IntegrationEventRegistry registry = new IntegrationEventRegistry();

        assertThatThrownBy(() -> registry.definitionOf(TestShipmentCreated.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(TestShipmentCreated.class.getName());
    }

    @Test
    void shouldThrowExceptionWhenTopicIsBlank() {
        final IntegrationEventRegistry registry = new IntegrationEventRegistry();

        assertThatThrownBy(() -> registry.register(TestEventWithoutTopic.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("topic");
    }

    @IntegrationEventType(value = "shipment.created", version = 1, topic = "shipment.created")
    record TestShipmentCreated(String shipmentId) implements IntegrationEvent, IntegrationEventKey {

        @Override
        public String eventKey() {
            return this.shipmentId;
        }
    }

    @IntegrationEventType(value = "shipment.created", version = 1, topic = "shipment.events")
    record TestDuplicatedShipmentCreated() implements IntegrationEvent {
    }

    @IntegrationEventType(value = "shipment.created", version = 2, topic = "shipment.created")
    record TestShipmentCreatedV2() implements IntegrationEvent {
    }

    @IntegrationEventType(value = "audit-log.entry", version = 1, topic = "audit-log.entries")
    record TestAuditLogEntry() implements IntegrationEvent {
    }

    @IntegrationEventType(value = "shipment.lost", version = 1, topic = " ")
    record TestEventWithoutTopic() implements IntegrationEvent {
    }

    record TestUndeclaredEvent() implements IntegrationEvent {
    }
}
