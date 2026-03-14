package com.warehouse.shipment.infrastructure.dto;

public record ShipmentRejectResponseItemDto(Long shipmentId,
                                            Long newShipmentId,
                                            Boolean loggedInTracker,
                                            Boolean success,
                                            String errorMessage) {
}
