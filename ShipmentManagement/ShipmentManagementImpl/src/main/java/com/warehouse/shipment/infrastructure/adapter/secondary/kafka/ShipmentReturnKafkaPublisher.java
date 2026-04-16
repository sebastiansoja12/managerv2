package com.warehouse.shipment.infrastructure.adapter.secondary.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShipmentReturnKafkaPublisher {

    private static final String SYSTEM_RETURN_REASON = "RETURN";
    private static final Long SYSTEM_USER_ID = 0L;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    public ShipmentReturnKafkaPublisher(final KafkaTemplate<String, String> kafkaTemplate,
                                        final ObjectMapper objectMapper,
                                        @Value("${manager.kafka.topics.shipment-return-created:shipment.return.created}")
                                        final String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    public void publish(final ShipmentSnapshot snapshot, final String returnDepartmentCode) {
        final ShipmentReturnMessage message = new ShipmentReturnMessage(
                snapshot.shipmentId().getValue(),
                SYSTEM_RETURN_REASON,
                ReasonCode.NO_LONGER_NEEDED.name(),
                returnDepartmentCode,
                SYSTEM_USER_ID
        );

        try {
            final String payload = this.objectMapper.writeValueAsString(message);
            this.kafkaTemplate.send(this.topic, String.valueOf(message.shipmentId()), payload);
            log.info("Published shipment return event for shipment {} to topic {}", message.shipmentId(), this.topic);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize shipment return event", exception);
        }
    }

    public record ShipmentReturnMessage(Long shipmentId,
                                        String reason,
                                        String reasonCode,
                                        String departmentCode,
                                        Long userId) {
    }
}
