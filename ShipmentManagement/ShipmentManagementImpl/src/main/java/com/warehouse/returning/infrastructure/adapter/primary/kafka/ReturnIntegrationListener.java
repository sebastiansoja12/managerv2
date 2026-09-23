package com.warehouse.returning.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.returning.api.event.ReturnPackageStatusChangedIntegrationEvent;
import com.warehouse.returning.api.event.ReturnProcessingStartedIntegrationEvent;
import com.warehouse.returning.api.event.ReturnCancelledIntegrationEvent;
import com.warehouse.returning.application.port.primary.command.ApplyReturnCancelledCommand;
import com.warehouse.returning.application.port.primary.ReturnProcessingPort;
import com.warehouse.returning.application.port.primary.command.ApplyReturnProcessingStartedCommand;
import org.springframework.stereotype.Component;

@Component
public class ReturnIntegrationListener {
    private final ReturnProcessingPort returnProcessingPort;

    public ReturnIntegrationListener(final ReturnProcessingPort returnProcessingPort) {
        this.returnProcessingPort = returnProcessingPort;
    }

    @KafkaEventListener(topics = "${manager.kafka.topics.return-package-status-changed}",
            groupId = "shipment-management")
    public void handle(final ReturnPackageStatusChangedIntegrationEvent event) {
        returnProcessingPort.applyStatusChanged(event.getShipmentId(), event.getReturnStatus());
    }

    @KafkaEventListener(topics = "${manager.kafka.topics.return-processing:return.processing.started}",
            groupId = "shipment-management")
    public void handle(final ReturnProcessingStartedIntegrationEvent event) {
        returnProcessingPort.applyProcessingStarted(new ApplyReturnProcessingStartedCommand(
                event.eventId(), event.shipmentId(), event.returnPackageId(), event.departmentId(),
                event.operatorId(), event.userId()));
    }

    @KafkaEventListener(topics = "${manager.kafka.topics.return-cancelled:return.cancelled}",
            groupId = "shipment-management")
    public void handle(final ReturnCancelledIntegrationEvent event) {
        returnProcessingPort.applyCancelled(new ApplyReturnCancelledCommand(
                event.eventId(), event.shipmentId(), event.returnPackageId(), event.departmentId(),
                event.operatorId(), event.userId()));
    }
}
