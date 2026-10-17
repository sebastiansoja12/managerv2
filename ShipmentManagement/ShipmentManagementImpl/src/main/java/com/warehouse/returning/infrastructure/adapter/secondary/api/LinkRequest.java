package com.warehouse.returning.infrastructure.adapter.secondary.api;

import com.warehouse.commonassets.identificator.ShipmentId;

public record LinkRequest(ShipmentId returnShipmentId) {
}
