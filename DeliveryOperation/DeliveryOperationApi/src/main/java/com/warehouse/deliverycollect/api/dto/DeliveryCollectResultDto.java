package com.warehouse.deliverycollect.api.dto;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record DeliveryCollectResultDto(TrackingNumber trackingNumber,
                                       ShipmentId shipmentId,
                                       boolean collected,
                                       String errorMessage) {
}
