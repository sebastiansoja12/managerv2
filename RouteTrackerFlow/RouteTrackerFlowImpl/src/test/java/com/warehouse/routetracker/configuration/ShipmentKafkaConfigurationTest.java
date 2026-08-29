package com.warehouse.routetracker.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.messaging.Message;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentKafkaConfigurationTest {

    @Test
    void shouldDeserializeFromListenerTypeWithoutProducerTypeHeader() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        final RecordMessageConverter converter = new ShipmentKafkaConfiguration()
                .shipmentKafkaRecordMessageConverter(objectMapper);
        final ShipmentCreatedIntegrationEvent expected = new ShipmentCreatedIntegrationEvent(
                new ShipmentSnapshot(
                        new ShipmentId(123L),
                        new SenderSnapshot("Jan", "Kowalski", "jan@example.com", "123456789",
                                "Warsaw", "00-001", "Main 1"),
                        new RecipientSnapshot("Anna", "Nowak", null, null, null, null, null),
                        ShipmentSize.SMALL,
                        new DepartmentCode("KT1"),
                        new DepartmentId(10L),
                        ShipmentStatus.CREATED,
                        ShipmentType.PARENT,
                        null,
                        new MoneySnapshot(BigDecimal.TEN, Currency.PLN),
                        LocalDateTime.parse("2026-08-11T10:15:30"),
                        LocalDateTime.parse("2026-08-11T10:15:30"),
                        false,
                        null,
                        false,
                        ShipmentPriority.MEDIUM,
                        CountryCode.PL,
                        CountryCode.DE,
                        null,
                        new TrackingNumber("TRACKING-123"),
                        new ExternalId<>(UUID.fromString("f211c97c-a4aa-4497-a7d9-92c9e5dc8bd6"))),
                new UserId(42L),
                new DepartmentId(10L),
                new OperatorId(7L)
        );
        final String producerJson = """
                {
                  "eventId": "f783d37e-06e9-4efc-8f18-099343b150e8",
                  "eventType": "shipment.changed",
                  "version": 1,
                  "occurredAt": "2026-08-11T10:15:30Z",
                  "payload": {
                    "shipmentId": {"value": 123},
                    "sender": {
                      "firstName": "Jan",
                      "lastName": "Kowalski",
                      "email": "jan@example.com",
                      "telephoneNumber": "123456789",
                      "city": "Warsaw",
                      "postalCode": "00-001",
                      "street": "Main 1"
                    },
                    "recipient": {
                      "firstName": "Anna",
                      "lastName": "Nowak"
                    },
                    "shipmentSize": "SMALL",
                    "destination": {"value": "KT1"},
                    "originDepartmentId": {"value": 10},
                    "trackingNumber": {"value": "TRACKING-123"},
                    "shipmentStatus": "CREATED",
                    "shipmentType": "PARENT",
                    "shipmentRelatedId": null,
                    "price": {"amount": 10, "currency": "PLN"},
                    "createdAt": "2026-08-11T10:15:30",
                    "updatedAt": "2026-08-11T10:15:30",
                    "locked": false,
                    "dangerousGood": null,
                    "signatureRequired": false,
                    "shipmentPriority": "MEDIUM",
                    "originCountry": "PL",
                    "destinationCountry": "DE",
                    "signature": null,
                    "externalShipmentId": {"value": "f211c97c-a4aa-4497-a7d9-92c9e5dc8bd6"}
                  },
                  "userId": {"value": 42},
                  "departmentId": {"value": 10},
                  "operatorId": {"value": 7}
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
