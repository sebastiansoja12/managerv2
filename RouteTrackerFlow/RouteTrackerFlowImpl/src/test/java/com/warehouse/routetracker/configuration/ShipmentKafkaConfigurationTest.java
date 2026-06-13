package com.warehouse.routetracker.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentChangedEventPayload;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.messaging.Message;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentKafkaConfigurationTest {

    @Test
    void shouldDeserializeFromListenerTypeWithoutProducerTypeHeader() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        final RecordMessageConverter converter = new ShipmentKafkaConfiguration()
                .shipmentKafkaRecordMessageConverter(objectMapper);
        final ShipmentCreatedIntegrationEvent expected = new ShipmentCreatedIntegrationEvent(
                new ShipmentChangedEventPayload(
                        new ShipmentId(123L),
                        "shipment.created",
                        ShipmentStatus.CREATED,
                        LocalDateTime.parse("2026-08-11T10:15:30"),
                        new OperatorId(7L),
                        new DepartmentId(10L),
                        new UserId(42L))
        );
        final String producerJson = """
                {
                  "eventId": "f783d37e-06e9-4efc-8f18-099343b150e8",
                  "eventType": "shipment.created",
                  "version": 1,
                  "occurredAt": "2026-08-11T10:15:30Z",
                  "payload": {
                    "shipmentId": {"value": 123},
                    "eventType": "shipment.created",
                    "shipmentStatus": "CREATED",
                    "changedAt": "2026-08-11T10:15:30",
                    "operatorId": {"value": 7},
                    "departmentId": {"value": 10},
                    "userId": {"value": 42}
                  }
                }
                """;
        final ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "shipment.events", 0, 0L, "123", producerJson);

        final Message<?> converted = converter.toMessage(
                record,
                null,
                null,
                ShipmentCreatedIntegrationEvent.class
        );

        final ShipmentCreatedIntegrationEvent event = (ShipmentCreatedIntegrationEvent) converted.getPayload();
        assertThat(record.headers().lastHeader("__TypeId__")).isNull();
        assertThat(event.payload()).isEqualTo(expected.payload());
        assertThat(event.userId()).isEqualTo(expected.userId());
        assertThat(event.departmentId()).isEqualTo(expected.departmentId());
        assertThat(event.operatorId()).isEqualTo(expected.operatorId());
    }
}
