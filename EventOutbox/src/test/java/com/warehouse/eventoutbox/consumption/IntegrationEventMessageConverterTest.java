package com.warehouse.eventoutbox.consumption;

import static com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.EVENTS_TOPIC;
import static com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.record;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.TestShipmentChanged;
import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.TestShipmentDestinationChanged;
import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.TestShipmentStatusChanged;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;

import tools.jackson.databind.json.JsonMapper;

class IntegrationEventMessageConverterTest {

    private static final String PAYLOAD = "{\"shipmentNumber\":\"SH-1\"}";

    private final IntegrationEventRegistry registry = IntegrationEventConsumptionFixture.registry();
    private final IntegrationEventMessageConverter converter = new IntegrationEventMessageConverter(
            JsonMapper.builder().build(), new IntegrationEventTypeResolver(() -> this.registry));

    @Test
    void shouldDeserializeSubclassDeclaredInHeaderWhenListenerExpectsBaseType() {
        final Object payload = this.converter.toMessage(record(EVENTS_TOPIC, PAYLOAD, Map.of(
                "ce_type", "test.shipment.destination.changed", "eventVersion", "1")),
                null, null, TestShipmentChanged.class).getPayload();

        assertThat(payload).isInstanceOf(TestShipmentDestinationChanged.class);
        assertThat(((TestShipmentChanged) payload).getShipmentNumber()).isEqualTo("SH-1");
    }

    @Test
    void shouldDeserializeToParameterTypeWhenRecordHasNoTypeHeaders() {
        final Object payload = this.converter.toMessage(record(EVENTS_TOPIC, PAYLOAD, Map.of()),
                null, null, TestShipmentStatusChanged.class).getPayload();

        assertThat(payload).isExactlyInstanceOf(TestShipmentStatusChanged.class);
    }

    @Test
    void shouldIgnoreHeaderTypeThatIsNotAssignableToParameter() {
        final Object payload = this.converter.toMessage(record(EVENTS_TOPIC, PAYLOAD, Map.of(
                "ce_type", "test.shipment.destination.changed")),
                null, null, TestShipmentStatusChanged.class).getPayload();

        assertThat(payload).isExactlyInstanceOf(TestShipmentStatusChanged.class);
    }
}
