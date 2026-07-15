package com.warehouse.returning.infrastructure.adapter.primary.kafka;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.domain.vo.UserId;
import com.warehouse.returning.infrastructure.adapter.primary.kafka.event.ShipmentReturnCanceled;
import com.warehouse.returning.infrastructure.adapter.primary.kafka.event.ShipmentReturnCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ShipmentReturnKafkaListener {

    private final ReturnPort returnPort;

    public ShipmentReturnKafkaListener(final ReturnPort returnPort) {
        this.returnPort = returnPort;
    }

    @KafkaListener(
            topics = "${manager.kafka.topics.shipment-return-created:shipment.return.created}",
            groupId = "${spring.kafka.consumer.group-id:returning-track-manager}"
    )
    public void handle(final ShipmentReturnCreated event) {
        final ReturnRequest request = this.toReturnRequest(event);
        this.returnPort.process(request);
        log.info("Processed shipment return created event for shipment {}", event.shipmentId().value());
    }

    @KafkaListener(
            topics = "${manager.kafka.topics.shipment-return-canceled:shipment.return.canceled}",
            groupId = "${spring.kafka.consumer.group-id:returning-track-manager}"
    )
    public void handle(final ShipmentReturnCanceled event) {
        this.returnPort.cancel(event.getShipmentId());
        log.info("Processed shipment return canceled event for shipment {}", event.getShipmentId().value());
    }

    private ReturnRequest toReturnRequest(final ShipmentReturnCreated event) {
        final DepartmentCode departmentCode = event.departmentCode();
        final UserId userId = event.userId();
        final ShipmentId shipmentId = event.shipmentId();
        final ReasonCode reasonCode = ReasonCode.valueOf(event.reasonCode());
        final ReturnPackageRequest returnPackageRequest = new ReturnPackageRequest(
                departmentCode,
                event.reason(),
                shipmentId,
                userId,
                reasonCode
        );

        return new ReturnRequest(departmentCode, userId, event.operatorId().value(), List.of(returnPackageRequest));
    }
}
