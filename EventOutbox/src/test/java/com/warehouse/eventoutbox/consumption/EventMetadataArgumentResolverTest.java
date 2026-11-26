package com.warehouse.eventoutbox.consumption;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.TestListeners;
import com.warehouse.eventoutbox.integration.model.EventMetadata;
import com.warehouse.eventoutbox.integration.model.EventId;

class EventMetadataArgumentResolverTest {

    private static final UUID EVENT_ID = UUID.fromString("3f1c2b8e-5d4a-4c1b-9a77-0f2e6d8c9b10");
    private static final Instant OCCURRED_AT = Instant.parse("2026-11-18T10:00:00Z");

    private final EventMetadataArgumentResolver resolver = new EventMetadataArgumentResolver();

    @Test
    void shouldSupportOnlyEventMetadataParameter() {
        assertThat(this.resolver.supportsParameter(parameter(1))).isTrue();
        assertThat(this.resolver.supportsParameter(parameter(0))).isFalse();
    }

    @Test
    void shouldBuildMetadataFromCloudEventsHeaders() {
        final Message<String> message = MessageBuilder.withPayload("{}")
                .setHeader("ce_id", bytes(EVENT_ID.toString()))
                .setHeader("ce_type", bytes("shipment.created"))
                .setHeader("ce_time", bytes(OCCURRED_AT.toString()))
                .setHeader("ce_source", bytes("manager-application"))
                .setHeader("ce_subject", bytes("SH-1"))
                .setHeader("eventVersion", bytes("2"))
                .setHeader("operatorId", bytes("7"))
                .setHeader("userId", bytes("11"))
                .setHeader("departmentId", "13")
                .setHeader("traceparent", bytes("00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01"))
                .build();

        final EventMetadata metadata = (EventMetadata) this.resolver.resolveArgument(parameter(1), message);

        assertThat(metadata).isEqualTo(new EventMetadata(new EventId(EVENT_ID), "shipment.created", 2, OCCURRED_AT,
                "manager-application", "SH-1", Map.of("operatorId", "7", "userId", "11", "departmentId", "13")));
    }

    @Test
    void shouldFallBackToLegacyHeaders() {
        final Message<String> message = MessageBuilder.withPayload("{}")
                .setHeader("eventId", bytes(EVENT_ID.toString()))
                .setHeader("eventType", bytes("shipment.created"))
                .setHeader("occurredAt", bytes(OCCURRED_AT.toString()))
                .build();

        final EventMetadata metadata = (EventMetadata) this.resolver.resolveArgument(parameter(1), message);

        assertThat(metadata.eventId()).isEqualTo(new EventId(EVENT_ID));
        assertThat(metadata.eventType()).isEqualTo("shipment.created");
        assertThat(metadata.eventVersion()).isEqualTo(1);
        assertThat(metadata.occurredAt()).isEqualTo(OCCURRED_AT);
        assertThat(metadata.attributes()).isEmpty();
    }

    private MethodParameter parameter(final int index) {
        return new MethodParameter(Arrays.stream(TestListeners.class.getMethods())
                .filter(method -> method.getName().equals("onDestinationChanged"))
                .findFirst()
                .orElseThrow(), index);
    }

    private byte[] bytes(final String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
