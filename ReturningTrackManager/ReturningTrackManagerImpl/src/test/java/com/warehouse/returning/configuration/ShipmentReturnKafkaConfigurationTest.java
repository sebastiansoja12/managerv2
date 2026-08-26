package com.warehouse.returning.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.returning.infrastructure.adapter.primary.kafka.event.IgnoredShipmentEvent;
import com.warehouse.returning.infrastructure.adapter.primary.kafka.event.ShipmentReturnCanceled;
import com.warehouse.returning.infrastructure.adapter.primary.kafka.event.ShipmentReturnCreated;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.messaging.Message;

class ShipmentReturnKafkaConfigurationTest {

    private static final Type MESSAGE_TYPE = Object.class;

    private final RecordMessageConverter converter =
            new ShipmentReturnKafkaConfiguration()
                    .shipmentReturnKafkaRecordMessageConverter(new ObjectMapper().findAndRegisterModules());

    @Test
    void shouldConvertShipmentReturnCreatedTypeIdToLocalEventClass() {
        final Message<?> message = this.converter.toMessage(
                this.record("ShipmentReturnCreated", """
                        {
                          "snapshot": {
                            "shipmentId": {
                              "value": 123
                            },
                            "shipmentStatus": "RETURN"
                          },
                          "reasonCode": "NO_LONGER_NEEDED",
                          "reason": "RETURN",
                          "departmentCode": {
                            "value": "WRO"
                          },
                          "userId": {
                            "value": 0
                          },
                          "operatorId": {
                            "value": 77
                          },
                          "timestamp": "2026-08-23T08:00:00Z"
                        }
                        """),
                null,
                null,
                MESSAGE_TYPE);

        assertThat(message.getPayload()).isInstanceOf(ShipmentReturnCreated.class);
    }

    @Test
    void shouldConvertShipmentReturnCanceledTypeIdToLocalEventClass() {
        final Message<?> message = this.converter.toMessage(
                this.record("ShipmentReturnCanceled", """
                        {
                          "snapshot": {
                            "shipmentId": {
                              "value": 123
                            },
                            "shipmentStatus": "DELIVERY"
                          },
                          "userId": {
                            "value": 0
                          },
                          "operatorId": {
                            "value": 77
                          },
                          "timestamp": "2026-08-23T08:00:00Z"
                        }
                        """),
                null,
                null,
                MESSAGE_TYPE);

        assertThat(message.getPayload()).isInstanceOf(ShipmentReturnCanceled.class);
    }

    @Test
    void shouldConvertNonReturnShipmentTypeIdToIgnoredEventClass() {
        final Message<?> message = this.converter.toMessage(
                this.record("ShipmentCreatedEvent", """
                        {
                          "snapshot": {
                            "shipmentId": {
                              "value": 123
                            },
                            "shipmentStatus": "CREATED"
                          },
                          "userId": {
                            "value": 0
                          },
                          "operatorId": {
                            "value": 77
                          },
                          "timestamp": "2026-08-23T08:00:00Z"
                        }
                        """),
                null,
                null,
                MESSAGE_TYPE);

        assertThat(message.getPayload()).isInstanceOf(IgnoredShipmentEvent.class);
    }

    private ConsumerRecord<String, String> record(final String typeId, final String payload) {
        final ConsumerRecord<String, String> record = new ConsumerRecord<>("shipment.events", 0, 0, "key", payload);
        record.headers().add(new RecordHeader("__TypeId__", typeId.getBytes(StandardCharsets.UTF_8)));
        return record;
    }
}
