package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary;

import static com.warehouse.eventoutbox.outbox.OutboxFixture.NOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.context.IntegrationEventContext;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxEventRepository;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxTracing;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;
import com.warehouse.eventoutbox.outbox.domain.service.OutboxEventFactory;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.mapper.OutboxEventHeadersMapper;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.mapper.OutboxEventPayloadMapper;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

class IntegrationEventOutboxAdapterTest {

    private static final IntegrationEventContext TENANT_CONTEXT = new IntegrationEventContext() {

        @Override
        public Map<String, String> attributes(final IntegrationEvent event) {
            return Map.of("tenantId", "7");
        }

        @Override
        public void enrichLegacyPayload(final ObjectNode payload, final Map<String, String> attributes) {
            payload.put("tenantId", attributes.get("tenantId"));
        }
    };

    private final JsonMapper jsonMapper = JsonMapper.builder().build();
    private final AtomicInteger relayWakeUps = new AtomicInteger();

    private final OutboxEventRepository repository = mock(OutboxEventRepository.class);
    private OutboxTracing tracing = OutboxTracing.NOOP;
    private IntegrationEventRegistry registry;

    @BeforeEach
    void setUp() {
        this.registry = new IntegrationEventRegistry();
        this.registry.register(TestShipmentCreated.class);
        this.registry.register(TestShipmentChanged.class);
    }

    @Test
    void shouldStorePurePayloadWithCloudEventsAndContextHeaders() {
        final IntegrationEventOutboxAdapter adapter = adapter(TENANT_CONTEXT, false);

        adapter.publish(new TestShipmentCreated("SH-1"));

        assertThat(stored()).satisfies(outboxEvent -> {
            assertThat(outboxEvent.getTopic()).isEqualTo("shipment.created");
            assertThat(outboxEvent.getMessageKey()).isEqualTo("SH-1");
            assertThat(outboxEvent.getPayload()).isEqualTo("{\"shipmentNumber\":\"SH-1\"}");
            assertThat(outboxEvent.getHeaders())
                    .containsEntry("ce_specversion", "1.0")
                    .containsEntry("ce_id", outboxEvent.getEventId().value().toString())
                    .containsEntry("ce_type", "shipment.created")
                    .containsEntry("ce_source", "manager-application")
                    .containsEntry("ce_time", NOW.toString())
                    .containsEntry("ce_subject", "SH-1")
                    .containsEntry("eventVersion", "1")
                    .containsEntry("tenantId", "7")
                    .doesNotContainKey("__TypeId__");
        });
    }

    @Test
    void shouldStoreLegacyEnvelopeExpectedByCurrentConsumers() {
        final IntegrationEventOutboxAdapter adapter = adapter(TENANT_CONTEXT, true);

        adapter.publish(new TestShipmentChanged("SH-1"));

        final OutboxEvent outboxEvent = stored();
        assertThat(outboxEvent.getHeaders())
                .containsEntry("__TypeId__", TestShipmentChanged.class.getName())
                .containsEntry("eventId", outboxEvent.getEventId().value().toString())
                .containsEntry("eventType", "shipment.changed")
                .containsEntry("occurredAt", NOW.toString())
                .containsEntry("ce_type", "shipment.changed");
        final JsonNode payload = this.jsonMapper.readTree(outboxEvent.getPayload());
        assertThat(payload.get("shipmentNumber").asString()).isEqualTo("SH-1");
        assertThat(payload.get("eventId").asString()).isEqualTo(outboxEvent.getEventId().value().toString());
        assertThat(payload.get("eventType").asString()).isEqualTo("shipment.changed");
        assertThat(payload.get("version").asInt()).isEqualTo(1);
        assertThat(payload.get("occurredAt").asString()).isEqualTo(NOW.toString());
        assertThat(payload.get("tenantId").asString()).isEqualTo("7");
    }

    @Test
    void shouldStoreEventWithoutContextHeadersWhenServiceHasNoContext() {
        final IntegrationEventOutboxAdapter adapter = adapter(IntegrationEventContext.NONE, false);

        adapter.publish(new TestShipmentCreated("SH-1"));

        assertThat(stored().getHeaders()).doesNotContainKey("tenantId");
    }

    @Test
    void shouldStoreTraceContextOfCallingThread() {
        this.tracing = new OutboxTracing() {

            @Override
            public Map<String, String> currentContext() {
                return Map.of("traceparent", "00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01");
            }

            @Override
            public OutboxSendSpan startSend(final OutboxEvent event) {
                throw new UnsupportedOperationException();
            }
        };
        final IntegrationEventOutboxAdapter adapter = adapter(IntegrationEventContext.NONE, false);

        adapter.publish(new TestShipmentCreated("SH-1"));

        assertThat(stored().getHeaders())
                .containsEntry("traceparent", "00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01")
                .containsEntry("ce_type", "shipment.created");
    }

    @Test
    void shouldWakeUpRelayOnlyAfterCommit() {
        final IntegrationEventOutboxAdapter adapter = adapter(IntegrationEventContext.NONE, false);
        TransactionSynchronizationManager.initSynchronization();
        try {
            adapter.publish(new TestShipmentCreated("SH-1"));
            assertThat(this.relayWakeUps).hasValue(0);

            TransactionSynchronizationUtils.triggerAfterCommit();

            assertThat(this.relayWakeUps).hasValue(1);
        } finally {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void shouldThrowExceptionWhenEventIsNotRegistered() {
        final IntegrationEventOutboxAdapter adapter = adapter(IntegrationEventContext.NONE, false);

        assertThatThrownBy(() -> adapter.publish(new TestUnregisteredEvent()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(TestUnregisteredEvent.class.getName());
    }

    private OutboxEvent stored() {
        final ArgumentCaptor<OutboxEvent> outboxEvent = ArgumentCaptor.forClass(OutboxEvent.class);
        verify(this.repository).save(outboxEvent.capture());
        return outboxEvent.getValue();
    }

    private IntegrationEventOutboxAdapter adapter(final IntegrationEventContext context, final boolean legacyEnvelope) {
        return new IntegrationEventOutboxAdapter(
                this.registry,
                this.repository,
                new EventMetadataFactory(context, Clock.fixed(NOW, ZoneOffset.UTC), "manager-application"),
                new OutboxEventHeadersMapper(legacyEnvelope),
                new OutboxEventPayloadMapper(this.jsonMapper, legacyEnvelope, context),
                new OutboxEventFactory(),
                this.tracing,
                this.relayWakeUps::incrementAndGet);
    }

    @IntegrationEventType(value = "shipment.created", version = 1, topic = "shipment.created")
    record TestShipmentCreated(String shipmentNumber) implements IntegrationEvent, IntegrationEventKey {

        @Override
        public String eventKey() {
            return this.shipmentNumber;
        }
    }

    @IntegrationEventType(value = "shipment.changed", version = 1, topic = "shipment.events")
    static final class TestShipmentChanged implements IntegrationEvent {

        private final String shipmentNumber;

        TestShipmentChanged(final String shipmentNumber) {
            this.shipmentNumber = shipmentNumber;
        }

        public String getShipmentNumber() {
            return this.shipmentNumber;
        }
    }

    record TestUnregisteredEvent() implements IntegrationEvent {
    }
}
