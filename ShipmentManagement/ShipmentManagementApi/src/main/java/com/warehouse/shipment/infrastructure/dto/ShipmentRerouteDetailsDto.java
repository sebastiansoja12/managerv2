package com.warehouse.shipment.infrastructure.dto;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record ShipmentRerouteDetailsDto(ShipmentId shipmentId,
                                        TrackingNumber trackingNumber,
                                        ShipmentStatus status,
                                        boolean reroutable,
                                        ShipmentPartyDto sender,
                                        ShipmentPartyDto recipient) {
}
