package com.warehouse.shipment.infrastructure.adapter.primary.kafka;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.shipment.api.event.ShipmentDeliveryReadModelChanged;
import com.warehouse.shipment.api.event.ShipmentPalletReadModelChanged;
import com.warehouse.shipment.api.event.ShipmentReadModelChanged;
import com.warehouse.shipment.api.event.snapshot.ShipmentDeliveryReadModelData;
import com.warehouse.shipment.api.event.snapshot.ShipmentPalletReadModelData;
import com.warehouse.shipment.application.port.primary.ShipmentReadModelSyncPort;
import com.warehouse.shipment.domain.vo.ShipmentDeliveryDetails;
import org.springframework.stereotype.Component;

@Component
public class ShipmentIntegrationListener {
    private final ShipmentReadModelSyncPort shipmentReadModelSyncPort;

    public ShipmentIntegrationListener(final ShipmentReadModelSyncPort shipmentReadModelSyncPort) {
        this.shipmentReadModelSyncPort = shipmentReadModelSyncPort;
    }

    @IntegrationEventListener(group = "shipment-management")
    public void handle(final ShipmentReadModelChanged event) {
        shipmentReadModelSyncPort.syncReadModel(event.snapshot().shipmentId());
    }

    @IntegrationEventListener(group = "shipment-management-delivery")
    public void handle(final ShipmentDeliveryReadModelChanged event) {
        final ShipmentDeliveryReadModelData payload = event.payload();
        shipmentReadModelSyncPort.syncReadModel(payload.shipmentId(), new ShipmentDeliveryDetails(
                payload.originDepartmentId(), payload.destinationDepartmentId(), payload.currentDepartmentId(),
                payload.plannedDeliveryAt(), payload.deliveredAt(), payload.courierCode(), payload.courierName()));
    }

    @IntegrationEventListener(group = "shipment-management-pallet")
    public void handle(final ShipmentPalletReadModelChanged event) {
        final ShipmentPalletReadModelData payload = event.payload();
        shipmentReadModelSyncPort.syncPalletTrackingNumber(payload.shipmentId(), payload.palletTrackingNumber());
    }
}
