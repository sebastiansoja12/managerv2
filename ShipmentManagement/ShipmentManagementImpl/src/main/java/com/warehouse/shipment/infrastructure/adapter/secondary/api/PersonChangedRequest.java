package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.Person;

public record PersonChangedRequest(ShipmentId shipmentId, Person person) {
}
