package com.warehouse.shipment.application.port.primary.result;

import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.shipment.domain.vo.ShipmentDeliveryDetails;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public record ShipmentResult(ShipmentSnapshot snapshot, ShipmentDeliveryDetails deliveryDetails,
                             boolean incidentReported, PalletTrackingNumber palletTrackingNumber) {

    public ShipmentResult(final ShipmentSnapshot snapshot) {
        this(snapshot, null, false, null);
    }
}
