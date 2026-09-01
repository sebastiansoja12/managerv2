package com.warehouse.shipment.application.event.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.commonassets.identificator.ShipmentId;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipmentReadModelData(ShipmentId shipmentId) {
}
