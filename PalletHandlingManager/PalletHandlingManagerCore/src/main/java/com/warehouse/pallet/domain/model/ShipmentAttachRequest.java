package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;

import java.util.Set;

public class ShipmentAttachRequest {
    private Set<ShipmentId> shipmentIds;
    private PalletId palletId;

    public ShipmentAttachRequest(final Set<ShipmentId> shipmentIds, final PalletId palletId) {
        this.shipmentIds = shipmentIds;
        this.palletId = palletId;
    }

    public Set<ShipmentId> getShipmentIds() {
        return shipmentIds;
    }

    public void setShipmentIds(final Set<ShipmentId> shipmentIds) {
        this.shipmentIds = shipmentIds;
    }

    public PalletId getPalletId() {
        return palletId;
    }

    public void setPalletId(final PalletId palletId) {
        this.palletId = palletId;
    }
}
