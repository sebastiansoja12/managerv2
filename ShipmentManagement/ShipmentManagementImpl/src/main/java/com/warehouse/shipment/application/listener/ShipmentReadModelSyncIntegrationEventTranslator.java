package com.warehouse.shipment.application.listener;

import com.warehouse.shipment.api.event.ShipmentReadModelChanged;
import com.warehouse.shipment.api.event.snapshot.ShipmentReadModelData;
import com.warehouse.shipment.domain.event.ShipmentChanged;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import org.springframework.stereotype.Component;

@Component
public class ShipmentReadModelSyncIntegrationEventTranslator {

    @IntegrationEventTranslation
    public ShipmentReadModelChanged translate(final ShipmentChanged event) {
        return new ShipmentReadModelChanged(
                        new ShipmentReadModelData(event.getSnapshot().shipmentId()),
                        event.getTimestamp());
    }
}
