package com.warehouse.routetracker.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.messaging.Message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentEventMessage;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.MoneySnapshot;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.RecipientSnapshot;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.SenderSnapshot;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentSnapshot;

class ShipmentKafkaConfigurationTest {

    @Test
    void shouldDeserializeFromListenerTypeWithoutProducerTypeHeader() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        final RecordMessageConverter converter = new ShipmentKafkaConfiguration()
                .shipmentKafkaRecordMessageConverter(objectMapper);
        final ShipmentEventMessage expected = new ShipmentEventMessage(
                UUID.fromString("f783d37e-06e9-4efc-8f18-099343b150e8"),
                "shipment.created",
                1,
                Instant.parse("2026-08-11T10:15:30Z"),
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
                OperatorId.of(7L)
        );
        final String producerJson = """
                {
                  "eventId": "f783d37e-06e9-4efc-8f18-099343b150e8",
                  "eventType": "shipment.created",
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

        final Message<?> converted = converter.toMessage(record, null, null, ShipmentEventMessage.class);

        assertThat(converted.getPayload()).isEqualTo(expected);
        assertThat(record.headers().lastHeader("__TypeId__")).isNull();
    }
}
