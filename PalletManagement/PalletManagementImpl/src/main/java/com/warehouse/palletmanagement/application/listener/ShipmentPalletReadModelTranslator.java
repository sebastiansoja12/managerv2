package com.warehouse.palletmanagement.application.listener;

import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.domain.event.PalletLoaded;
import com.warehouse.palletmanagement.domain.event.PalletLoadingReverted;
import com.warehouse.palletmanagement.domain.event.PalletUnloaded;
import com.warehouse.shipment.api.event.ShipmentPalletReadModelChanged;
import com.warehouse.shipment.api.event.snapshot.ShipmentPalletReadModelData;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShipmentPalletReadModelTranslator {

    @IntegrationEventTranslation
    public List<ShipmentPalletReadModelChanged> translate(final PalletLoaded event) {
        return shipmentPallets(event.getSnapshot().shipmentIds(), event.getSnapshot().trackingNumber());
    }

    @IntegrationEventTranslation
    public List<ShipmentPalletReadModelChanged> translate(final PalletLoadingReverted event) {
        return shipmentPallets(event.getSnapshot().shipmentIds(), null);
    }

    @IntegrationEventTranslation
    public List<ShipmentPalletReadModelChanged> translate(final PalletUnloaded event) {
        return shipmentPallets(event.getSnapshot().unloadedShipmentIds(), null);
    }

    private List<ShipmentPalletReadModelChanged> shipmentPallets(final List<ShipmentId> shipmentIds,
                                                                 final PalletTrackingNumber palletTrackingNumber) {
        return shipmentIds.stream()
                .map(shipmentId -> new ShipmentPalletReadModelChanged(
                        new ShipmentPalletReadModelData(shipmentId, palletTrackingNumber)))
                .toList();
    }
}
