package com.warehouse.shipment.application.port.primary.result;

import com.warehouse.shipment.domain.model.ShipmentNote;

public record ShipmentNoteResult(ShipmentNote note, String authorUsername) {
}
