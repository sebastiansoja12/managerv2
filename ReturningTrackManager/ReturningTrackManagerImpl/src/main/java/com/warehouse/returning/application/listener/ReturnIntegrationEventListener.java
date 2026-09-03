package com.warehouse.returning.application.listener;

import com.warehouse.returning.api.identificator.DepartmentId;
import com.warehouse.returning.api.identificator.OperatorId;
import com.warehouse.returning.api.identificator.ShipmentId;
import com.warehouse.returning.api.identificator.UserId;
import com.warehouse.returning.application.port.secondary.ReturnEventPublisherServicePort;
import com.warehouse.returning.api.event.ReturnProcessingStartedIntegrationEvent;
import com.warehouse.returning.api.event.ReturnCancelledIntegrationEvent;
import com.warehouse.returning.domain.event.ReturnPackageCanceled;
import com.warehouse.returning.domain.event.ReturnPackageProcessingStarted;
import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReturnIntegrationEventListener {

    private final ReturnEventPublisherServicePort integrationEventPublisher;

    public ReturnIntegrationEventListener(final ReturnEventPublisherServicePort integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @EventListener
    public void handle(final ReturnPackageProcessingStarted event) {
        final ReturnPackageSnapshot snapshot = event.getSnapshot();
        integrationEventPublisher.publish(new ReturnProcessingStartedIntegrationEvent(
                UUID.randomUUID(), new ShipmentId(snapshot.shipmentId().value()), snapshot.returnPackageId(),
                new DepartmentId(snapshot.assignedDepartmentId().value()),
                new OperatorId(snapshot.operatorId().value()),
                snapshot.processedBy() == null ? null : new UserId(snapshot.processedBy().value()),
                event.getTimestamp()));
    }

    @EventListener
    public void handle(final ReturnPackageCanceled event) {
        final ReturnPackageSnapshot snapshot = event.getSnapshot();
        integrationEventPublisher.publish(new ReturnCancelledIntegrationEvent(
                UUID.randomUUID(), new ShipmentId(snapshot.shipmentId().value()), snapshot.returnPackageId(),
                new DepartmentId(snapshot.assignedDepartmentId().value()),
                new OperatorId(snapshot.operatorId().value()),
                snapshot.processedBy() == null ? null : new UserId(snapshot.processedBy().value()),
                event.getTimestamp()));
    }
}
