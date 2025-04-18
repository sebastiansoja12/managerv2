package com.warehouse.shipment.domain.vo;


import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentCreateResponse(String routeProcessId, ShipmentId shipmentId) {
}
