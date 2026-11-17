package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.shipment.domain.model.Shipment;

public record ShipmentListEntry(Shipment shipment, ShipmentDeliveryDetails deliveryDetails, boolean incidentReported,
                                PalletTrackingNumber palletTrackingNumber) {
}
