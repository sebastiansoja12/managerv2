package com.warehouse.shipment.domain.vo;


import java.util.UUID;

import com.warehouse.commonassets.identificator.ExternalId;

public record ShipmentCreateResponse(ExternalId<UUID> shipmentId,
                                     String trackingNumber) {
}
