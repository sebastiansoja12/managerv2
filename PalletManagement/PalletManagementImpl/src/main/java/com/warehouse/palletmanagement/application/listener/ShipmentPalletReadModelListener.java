package com.warehouse.palletmanagement.application.listener;

import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.application.port.secondary.PalletIntegrationEventServicePort;
import com.warehouse.palletmanagement.domain.event.PalletLoaded;
import com.warehouse.palletmanagement.domain.event.PalletLoadingReverted;
import com.warehouse.palletmanagement.domain.event.PalletUnloaded;
import com.warehouse.shipment.api.event.ShipmentPalletReadModelChanged;
import com.warehouse.shipment.api.event.snapshot.ShipmentPalletReadModelData;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class ShipmentPalletReadModelListener {

    private final PalletIntegrationEventServicePort palletIntegrationEventServicePort;

    public ShipmentPalletReadModelListener(final PalletIntegrationEventServicePort palletIntegrationEventServicePort) {
        this.palletIntegrationEventServicePort = palletIntegrationEventServicePort;
    }

    @EventListener
    public void handle(final PalletLoaded event) {
        publishShipmentPallet(event.getSnapshot().shipmentIds(), event.getSnapshot().trackingNumber());
    }

    @EventListener
    public void handle(final PalletLoadingReverted event) {
        publishShipmentPallet(event.getSnapshot().shipmentIds(), null);
    }

    @EventListener
    public void handle(final PalletUnloaded event) {
        publishShipmentPallet(event.getSnapshot().unloadedShipmentIds(), null);
    }

    private void publishShipmentPallet(final List<ShipmentId> shipmentIds,
                                       final PalletTrackingNumber palletTrackingNumber) {
        shipmentIds.forEach(shipmentId -> this.palletIntegrationEventServicePort.publishEvent(
                new ShipmentPalletReadModelChanged(
                        new ShipmentPalletReadModelData(shipmentId, palletTrackingNumber))));
    }
}
