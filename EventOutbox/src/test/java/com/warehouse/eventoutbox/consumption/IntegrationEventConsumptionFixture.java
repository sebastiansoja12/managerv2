package com.warehouse.eventoutbox.consumption;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.EventMetadata;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;

public final class IntegrationEventConsumptionFixture {

    public static final String EVENTS_TOPIC = "test.shipment.events";
    public static final String CREATED_TOPIC = "test.shipment.created";

    private IntegrationEventConsumptionFixture() {
    }

    public static IntegrationEventRegistry registry() {
        final IntegrationEventRegistry registry = new IntegrationEventRegistry();
        registry.register(TestShipmentChanged.class);
        registry.register(TestShipmentCreated.class);
        registry.register(TestShipmentStatusChanged.class);
        registry.register(TestShipmentDestinationChanged.class);
        return registry;
    }

    public static ConsumerRecord<String, String> record(final String topic,
                                                        final String payload,
                                                        final Map<String, String> headers) {
        final ConsumerRecord<String, String> record = new ConsumerRecord<>(topic, 0, 0L, "SH-1", payload);
        headers.forEach((name, value) -> record.headers().add(name, value.getBytes(StandardCharsets.UTF_8)));
        return record;
    }

    @IntegrationEventType(value = "test.shipment.changed", version = 1, topic = EVENTS_TOPIC)
    public static class TestShipmentChanged implements IntegrationEvent {

        private String shipmentNumber;

        public String getShipmentNumber() {
            return this.shipmentNumber;
        }

        public void setShipmentNumber(final String shipmentNumber) {
            this.shipmentNumber = shipmentNumber;
        }
    }

    @IntegrationEventType(value = "test.shipment.created", version = 1, topic = CREATED_TOPIC)
    public static class TestShipmentCreated extends TestShipmentChanged {
    }

    @IntegrationEventType(value = "test.shipment.status.changed", version = 1, topic = EVENTS_TOPIC)
    public static class TestShipmentStatusChanged extends TestShipmentChanged {
    }

    @IntegrationEventType(value = "test.shipment.destination.changed", version = 1, topic = EVENTS_TOPIC)
    public static class TestShipmentDestinationChanged extends TestShipmentChanged {
    }

    public static class TestListeners {

        @IntegrationEventListener(group = "test-delivery")
        public void onCreated(final TestShipmentCreated event) {
        }

        @IntegrationEventListener(group = "test-delivery")
        public void onDestinationChanged(final TestShipmentDestinationChanged event, final EventMetadata metadata) {
        }

        @IntegrationEventListener(group = "test-mail")
        public void onAnyChange(final TestShipmentChanged event) {
        }

        @IntegrationEventListener(group = "test-tracker", topics = EVENTS_TOPIC,
                types = "test.shipment.status.changed")
        public void onStatusChanged(final TestShipmentChanged event) {
        }
    }

    public static class SubscriptionCases {

        @IntegrationEventListener(group = "test-audit", topics = EVENTS_TOPIC)
        public void onChanged(final TestShipmentChanged event) {
        }
    }

    public static class StatusChangedListener {

        @IntegrationEventListener(group = "test-delivery")
        public void onStatusChanged(final TestShipmentStatusChanged event) {
        }
    }

    public static class OtherStatusChangedListener {

        @IntegrationEventListener(group = "test-delivery")
        public void onStatusChanged(final TestShipmentStatusChanged event) {
        }
    }

    public static class AmbiguousListeners {

        @IntegrationEventListener(group = "test-delivery")
        public void onAnyChange(final TestShipmentChanged event) {
        }

        @IntegrationEventListener(group = "test-delivery")
        public void onStatusChanged(final TestShipmentStatusChanged event) {
        }
    }
}
