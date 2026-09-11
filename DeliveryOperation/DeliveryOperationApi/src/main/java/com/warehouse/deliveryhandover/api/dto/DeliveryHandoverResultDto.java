package com.warehouse.deliveryhandover.api.dto;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record DeliveryHandoverResultDto(TrackingNumber trackingNumber,
                                        ShipmentId shipmentId,
                                        boolean handedOver,
                                        String errorMessage) {
}
