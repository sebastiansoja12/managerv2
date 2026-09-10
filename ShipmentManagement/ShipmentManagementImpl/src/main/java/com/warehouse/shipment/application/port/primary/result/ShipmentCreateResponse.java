package com.warehouse.shipment.application.port.primary.result;


import java.util.UUID;

import com.warehouse.commonassets.identificator.ExternalId;

public record ShipmentCreateResponse(ExternalId<UUID> shipmentId,
                                     String trackingNumber) {
}
