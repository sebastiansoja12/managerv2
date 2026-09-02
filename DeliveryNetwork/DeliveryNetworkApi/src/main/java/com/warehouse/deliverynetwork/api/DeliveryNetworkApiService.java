package com.warehouse.deliverynetwork.api;

import com.warehouse.deliverynetwork.api.dto.DepartmentIdDto;
import com.warehouse.deliverynetwork.api.dto.DeliveryNetworkDto;
import com.warehouse.deliverynetwork.api.dto.DeliveryPathDto;

public interface DeliveryNetworkApiService {

    DeliveryNetworkDto getCurrentNetwork();

    boolean areDirectlyConnected(
            final DepartmentIdDto firstDepartmentId,
            final DepartmentIdDto secondDepartmentId);

    DeliveryPathDto findDeliveryPath(
            final DepartmentIdDto sourceDepartmentId,
            final DepartmentIdDto targetDepartmentId);
}
