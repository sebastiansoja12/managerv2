package com.warehouse.palletmanagement.application.listener;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.api.event.PalletLoadedIntegrationEvent;
import com.warehouse.palletmanagement.api.event.PalletLoadingRevertedIntegrationEvent;
import com.warehouse.palletmanagement.api.event.snapshot.PalletLoadedEventData;
import com.warehouse.palletmanagement.api.event.snapshot.PalletLoadingRevertedEventData;
import com.warehouse.palletmanagement.application.port.secondary.PalletIntegrationEventServicePort;
import com.warehouse.palletmanagement.domain.event.PalletLoaded;
import com.warehouse.palletmanagement.domain.event.PalletLoadingReverted;
import com.warehouse.palletmanagement.domain.vo.PalletLoadedSnapshot;
import com.warehouse.palletmanagement.domain.vo.PalletLoadingRevertedSnapshot;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class PalletIntegrationEventListener {

    private final PalletIntegrationEventServicePort palletIntegrationEventServicePort;

    public PalletIntegrationEventListener(final PalletIntegrationEventServicePort palletIntegrationEventServicePort) {
        this.palletIntegrationEventServicePort = palletIntegrationEventServicePort;
    }

    @EventListener
    public void handle(final PalletLoaded event) {
        final PalletLoadedSnapshot snapshot = event.getSnapshot();
        final PalletLoadedEventData eventData = new PalletLoadedEventData(
                snapshot.palletId().value(),
                snapshot.trackingNumber().value(),
                snapshot.shipmentIds().stream().map(ShipmentId::getValue).toList(),
                departmentValue(snapshot.originDepartmentId()),
                departmentValue(snapshot.destinationDepartmentId()));
        this.palletIntegrationEventServicePort.publishEvent(new PalletLoadedIntegrationEvent(eventData));
    }

    @EventListener
    public void handle(final PalletLoadingReverted event) {
        final PalletLoadingRevertedSnapshot snapshot = event.getSnapshot();
        final PalletLoadingRevertedEventData eventData = new PalletLoadingRevertedEventData(
                snapshot.palletId().value(),
                snapshot.trackingNumber().value(),
                snapshot.shipmentIds().stream().map(ShipmentId::getValue).toList());
        this.palletIntegrationEventServicePort.publishEvent(new PalletLoadingRevertedIntegrationEvent(eventData));
    }

    private static Long departmentValue(final DepartmentId departmentId) {
        return departmentId == null ? null : departmentId.value();
    }
}
