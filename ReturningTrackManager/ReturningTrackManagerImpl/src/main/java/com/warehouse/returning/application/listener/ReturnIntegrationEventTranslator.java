package com.warehouse.returning.application.listener;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.returning.api.event.ReturnCancelledIntegrationEvent;
import com.warehouse.returning.api.event.ReturnCompletedIntegrationEvent;
import com.warehouse.returning.api.event.ReturnProcessingStartedIntegrationEvent;
import com.warehouse.returning.api.event.ReturnReadModelChangedIntegrationEvent;
import com.warehouse.returning.api.identificator.DepartmentId;
import com.warehouse.returning.api.identificator.OperatorId;
import com.warehouse.returning.api.identificator.ShipmentId;
import com.warehouse.returning.api.identificator.UserId;
import com.warehouse.returning.domain.event.ReturnPackageCanceled;
import com.warehouse.returning.domain.event.ReturnPackageChanged;
import com.warehouse.returning.domain.event.ReturnPackageCompleted;
import com.warehouse.returning.domain.event.ReturnPackageProcessingStarted;
import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ReturnIntegrationEventTranslator {

    @IntegrationEventTranslation
    public List<IntegrationEvent> translate(final ReturnPackageProcessingStarted event) {
        final ReturnPackageSnapshot snapshot = event.getSnapshot();
        return List.of(new ReturnProcessingStartedIntegrationEvent(
                UUID.randomUUID(), shipmentId(snapshot), returnShipmentId(snapshot), snapshot.returnPackageId(),
                departmentId(snapshot), operatorId(snapshot), userId(snapshot), event.getTimestamp()),
                readModelChanged(event));
    }

    @IntegrationEventTranslation
    public List<IntegrationEvent> translate(final ReturnPackageCanceled event) {
        final ReturnPackageSnapshot snapshot = event.getSnapshot();
        return List.of(new ReturnCancelledIntegrationEvent(
                UUID.randomUUID(), shipmentId(snapshot), returnShipmentId(snapshot), snapshot.returnPackageId(),
                departmentId(snapshot), operatorId(snapshot), userId(snapshot), event.getTimestamp()),
                readModelChanged(event));
    }

    @IntegrationEventTranslation
    public List<IntegrationEvent> translate(final ReturnPackageCompleted event) {
        final ReturnPackageSnapshot snapshot = event.getSnapshot();
        return List.of(new ReturnCompletedIntegrationEvent(
                UUID.randomUUID(), shipmentId(snapshot), returnShipmentId(snapshot), snapshot.returnPackageId(),
                departmentId(snapshot), operatorId(snapshot), userId(snapshot), event.getTimestamp()),
                readModelChanged(event));
    }

    @IntegrationEventTranslation
    public ReturnReadModelChangedIntegrationEvent translate(final ReturnPackageChanged event) {
        return readModelChanged(event);
    }

    private static ReturnReadModelChangedIntegrationEvent readModelChanged(final ReturnPackageChanged event) {
        final ReturnPackageSnapshot snapshot = event.getSnapshot();
        return new ReturnReadModelChangedIntegrationEvent(
                UUID.randomUUID(), shipmentId(snapshot), returnShipmentId(snapshot), snapshot.returnPackageId(),
                departmentId(snapshot), operatorId(snapshot), userId(snapshot), event.getTimestamp());
    }

    private static ShipmentId shipmentId(final ReturnPackageSnapshot snapshot) {
        return new ShipmentId(snapshot.shipmentId().value());
    }

    private static ShipmentId returnShipmentId(final ReturnPackageSnapshot snapshot) {
        return snapshot.returnShipmentId() == null ? null : new ShipmentId(snapshot.returnShipmentId().value());
    }

    private static DepartmentId departmentId(final ReturnPackageSnapshot snapshot) {
        return new DepartmentId(snapshot.assignedDepartmentId().value());
    }

    private static OperatorId operatorId(final ReturnPackageSnapshot snapshot) {
        return new OperatorId(snapshot.operatorId().value());
    }

    private static UserId userId(final ReturnPackageSnapshot snapshot) {
        return snapshot.processedBy() == null ? null : new UserId(snapshot.processedBy().value());
    }
}
