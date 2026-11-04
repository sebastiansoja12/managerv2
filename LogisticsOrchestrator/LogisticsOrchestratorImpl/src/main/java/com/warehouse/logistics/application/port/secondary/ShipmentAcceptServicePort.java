package com.warehouse.logistics.application.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public interface ShipmentAcceptServicePort {

    ShipmentId acceptInDepartment(final TrackingNumber trackingNumber);
}
