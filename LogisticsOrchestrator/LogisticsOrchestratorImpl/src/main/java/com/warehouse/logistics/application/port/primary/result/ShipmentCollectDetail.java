package com.warehouse.logistics.application.port.primary.result;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record ShipmentCollectDetail(TrackingNumber trackingNumber,
                                    ShipmentId shipmentId,
                                    boolean collected,
                                    String errorMessage) {
}
