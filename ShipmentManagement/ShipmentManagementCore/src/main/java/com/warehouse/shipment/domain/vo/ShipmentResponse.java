package com.warehouse.shipment.domain.vo;


import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentResponse(String routeProcessId, ShipmentId shipmentId) {
}
