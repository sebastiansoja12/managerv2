package com.warehouse.eventoutbox.consumption;

import static com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.EVENTS_TOPIC;
import static com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.record;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.TestShipmentStatusChanged;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventDefinition;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;

class IntegrationEventTypeResolverTest {

    private final IntegrationEventRegistry registry = IntegrationEventConsumptionFixture.registry();
    private final IntegrationEventTypeResolver resolver = new IntegrationEventTypeResolver(() -> this.registry);

    @Test
    void shouldResolveTypeFromCloudEventsHeader() {
        assertThat(this.resolver.resolve(record(EVENTS_TOPIC, "{}", Map.of(
                "ce_type", "test.shipment.status.changed", "eventVersion", "1")).headers()))
                .map(IntegrationEventDefinition::type)
                .contains("test.shipment.status.changed");
    }

    @Test
    void shouldResolveTypeFromLegacyEventTypeHeader() {
        assertThat(this.resolver.resolve(record(EVENTS_TOPIC, "{}", Map.of(
                "eventType", "test.shipment.status.changed")).headers()))
                .map(IntegrationEventDefinition::type)
                .contains("test.shipment.status.changed");
    }

    @Test
    void shouldResolveTypeFromJavaTypeIdHeader() {
        assertThat(this.resolver.resolve(record(EVENTS_TOPIC, "{}", Map.of(
                "__TypeId__", TestShipmentStatusChanged.class.getName())).headers()))
                .map(IntegrationEventDefinition::type)
                .contains("test.shipment.status.changed");
    }

    @Test
    void shouldReturnEmptyWhenRecordHasNoTypeHeaders() {
        assertThat(this.resolver.resolve(record(EVENTS_TOPIC, "{}", Map.of()).headers())).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenRegistryIsNotAvailable() {
        final IntegrationEventTypeResolver withoutRegistry = new IntegrationEventTypeResolver(() -> null);

        assertThat(withoutRegistry.resolve(record(EVENTS_TOPIC, "{}", Map.of(
                "ce_type", "test.shipment.status.changed")).headers())).isEmpty();
    }
}
