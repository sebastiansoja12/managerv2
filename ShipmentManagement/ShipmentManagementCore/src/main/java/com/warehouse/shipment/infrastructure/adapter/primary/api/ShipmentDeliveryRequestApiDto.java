package com.warehouse.shipment.infrastructure.adapter.primary.api;

public record ShipmentDeliveryRequestApiDto(ShipmentIdDto shipmentId, String deliveryMethod,
                                            SupplierCodeDto supplierCode, String deliveryStatus) {
}
