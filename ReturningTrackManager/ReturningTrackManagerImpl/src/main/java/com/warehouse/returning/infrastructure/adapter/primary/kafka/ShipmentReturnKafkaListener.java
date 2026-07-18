package com.warehouse.returning.infrastructure.adapter.primary.kafka;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.domain.vo.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShipmentReturnKafkaListener {

    private final ObjectMapper objectMapper;
    private final ReturnPort returnPort;

    public ShipmentReturnKafkaListener(final ObjectMapper objectMapper, final ReturnPort returnPort) {
        this.objectMapper = objectMapper;
        this.returnPort = returnPort;
    }

    @KafkaListener(
            topics = "${manager.kafka.topics.shipment-return-created:shipment.return.created}",
            groupId = "${spring.kafka.consumer.group-id:returning-track-manager}"
    )
    public void handle(final String payload) {
        final ShipmentReturnMessage message = this.deserialize(payload);
        final ReturnRequest request = this.toReturnRequest(message);

        this.returnPort.process(request);
        log.info("Processed shipment return event for shipment {}", message.shipmentId());
    }

    private ShipmentReturnMessage deserialize(final String payload) {
        try {
            return this.objectMapper.readValue(payload, ShipmentReturnMessage.class);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot deserialize shipment return event", exception);
        }
    }

    private ReturnRequest toReturnRequest(final ShipmentReturnMessage message) {
        final DepartmentCode departmentCode = new DepartmentCode(message.departmentCode());
        final UserId userId = new UserId(message.userId());
        final ShipmentId shipmentId = new ShipmentId(message.shipmentId());
        final ReasonCode reasonCode = ReasonCode.valueOf(message.reasonCode());
        final ReturnPackageRequest returnPackageRequest = new ReturnPackageRequest(
                departmentCode,
                message.reason(),
                shipmentId,
                userId,
                reasonCode
        );

        return new ReturnRequest(departmentCode, userId, List.of(returnPackageRequest));
    }

    private record ShipmentReturnMessage(Long shipmentId,
                                         String reason,
                                         String reasonCode,
                                         String departmentCode,
                                         Long userId) {
    }
}
