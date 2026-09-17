package com.warehouse.deliverynetwork.application.port.secondary;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliverynetwork.domain.vo.ShipmentDepartments;

public interface ShipmentDeliveryServicePort {

    ShipmentDepartments getShipmentDepartments(final TrackingNumber trackingNumber);
}
