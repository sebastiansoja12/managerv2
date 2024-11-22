package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteRequest;

public interface RouteLogReturnServicePort {
    void logRouteLogReturnDelivery(final DeliveryReturnRouteRequest deliveryReturnRouteRequest);

    void logDepotCodeReturnDelivery(final DeliveryReturnRequest deliveryReturnRequest);

    void logSupplierCode(final DeliveryReturn deliveryReturn);
}
