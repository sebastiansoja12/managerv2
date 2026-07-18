package com.warehouse.shipment.infrastructure.adapter.secondary.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class ShipmentReturnKafkaPublisherTest {

    private static final String TOPIC = "shipment.return.created";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void shouldPublishShipmentReturnMessage() throws Exception {
        final ShipmentReturnKafkaPublisher publisher =
                new ShipmentReturnKafkaPublisher(this.kafkaTemplate, this.objectMapper, TOPIC);
        final ShipmentSnapshot snapshot = new ShipmentSnapshot(
                new ShipmentId(123L),
                null,
                null,
                null,
                "WRO",
                ShipmentStatus.RETURN,
                null,
                null,
                null,
                null,
                null,
                false,
                null,
                false,
                null,
                null,
                null,
                null,
                null,
                null
        );
        final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);

        publisher.publish(snapshot, "WRO");

        verify(this.kafkaTemplate).send(eq(TOPIC), eq("123"), payloadCaptor.capture());
        final JsonNode payload = this.objectMapper.readTree(payloadCaptor.getValue());
        assertThat(payload.get("shipmentId").asLong()).isEqualTo(123L);
        assertThat(payload.get("reason").asText()).isEqualTo("RETURN");
        assertThat(payload.get("reasonCode").asText()).isEqualTo("NO_LONGER_NEEDED");
        assertThat(payload.get("departmentCode").asText()).isEqualTo("WRO");
        assertThat(payload.get("userId").asLong()).isZero();
    }
}
