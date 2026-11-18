package com.warehouse.deliverynetwork.api;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliverynetwork.api.dto.DepartmentIdDto;
import com.warehouse.deliverynetwork.api.dto.DeliveryNetworkDto;
import com.warehouse.deliverynetwork.api.dto.DeliveryPathDto;
import com.warehouse.deliverynetwork.api.dto.ShipmentDeliveryRouteDto;

public interface DeliveryNetworkApiService {

    DeliveryNetworkDto getCurrentNetwork();

    boolean areDirectlyConnected(
            final DepartmentIdDto firstDepartmentId,
            final DepartmentIdDto secondDepartmentId);

    DeliveryPathDto findDeliveryPath(
            final DepartmentIdDto sourceDepartmentId,
            final DepartmentIdDto targetDepartmentId);

    ShipmentDeliveryRouteDto findShipmentDeliveryRoute(final TrackingNumber trackingNumber);
}
