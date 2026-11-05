package com.warehouse.deliverynetwork.application.port.primary.result;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;

import java.util.List;

public record ShipmentDeliveryRouteResult(
        ShipmentId shipmentId,
        TrackingNumber trackingNumber,
        List<DepartmentNode> departments,
        Double distanceInKilometers) {
}
