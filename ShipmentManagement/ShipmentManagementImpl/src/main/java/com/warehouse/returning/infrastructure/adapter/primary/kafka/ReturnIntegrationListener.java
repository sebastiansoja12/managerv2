package com.warehouse.returning.infrastructure.adapter.primary.kafka;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.returning.api.event.ReturnPackageStatusChangedIntegrationEvent;
import com.warehouse.returning.api.event.ReturnProcessingStartedIntegrationEvent;
import com.warehouse.returning.api.event.ReturnCancelledIntegrationEvent;
import com.warehouse.returning.api.event.ReturnCompletedIntegrationEvent;
import com.warehouse.returning.api.event.ReturnLifecycleIntegrationEvent;
import com.warehouse.returning.application.port.primary.command.ApplyReturnCancelledCommand;
import com.warehouse.returning.application.port.primary.command.ApplyReturnCompletedCommand;
import com.warehouse.returning.application.port.primary.ReturnProcessingPort;
import com.warehouse.returning.application.port.primary.command.ApplyReturnProcessingStartedCommand;
import org.springframework.stereotype.Component;

@Component
public class ReturnIntegrationListener {
    private final ReturnProcessingPort returnProcessingPort;

    public ReturnIntegrationListener(final ReturnProcessingPort returnProcessingPort) {
        this.returnProcessingPort = returnProcessingPort;
    }

    @IntegrationEventListener(group = "shipment-management")
    public void handle(final ReturnPackageStatusChangedIntegrationEvent event) {
        returnProcessingPort.applyStatusChanged(event.getShipmentId(), event.getReturnStatus());
    }

    @IntegrationEventListener(group = "shipment-management")
    public void handle(final ReturnProcessingStartedIntegrationEvent event) {
        returnProcessingPort.applyProcessingStarted(new ApplyReturnProcessingStartedCommand(
                event.eventId(), new ShipmentId(event.shipmentId().value()), returnShipmentId(event),
                event.returnPackageId(),
                new DepartmentId(event.departmentId().value()), new OperatorId(event.operatorId().value()),
                event.userId() == null ? null : new UserId(event.userId().value())));
    }

    @IntegrationEventListener(group = "shipment-management")
    public void handle(final ReturnCancelledIntegrationEvent event) {
        returnProcessingPort.applyCancelled(new ApplyReturnCancelledCommand(
                event.eventId(), new ShipmentId(event.shipmentId().value()), returnShipmentId(event),
                event.returnPackageId(),
                new DepartmentId(event.departmentId().value()), new OperatorId(event.operatorId().value()),
                event.userId() == null ? null : new UserId(event.userId().value())));
    }

    @IntegrationEventListener(group = "shipment-management")
    public void handle(final ReturnCompletedIntegrationEvent event) {
        returnProcessingPort.applyCompleted(new ApplyReturnCompletedCommand(
                event.eventId(), new ShipmentId(event.shipmentId().value()), returnShipmentId(event),
                event.returnPackageId(),
                new DepartmentId(event.departmentId().value()), new OperatorId(event.operatorId().value()),
                event.userId() == null ? null : new UserId(event.userId().value())));
    }

    private ShipmentId returnShipmentId(final ReturnLifecycleIntegrationEvent event) {
        return event.returnShipmentId() == null ? null : new ShipmentId(event.returnShipmentId().value());
    }
}
