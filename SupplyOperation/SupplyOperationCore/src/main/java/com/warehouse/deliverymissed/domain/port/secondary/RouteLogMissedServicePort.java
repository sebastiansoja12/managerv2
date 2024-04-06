package com.warehouse.deliverymissed.domain.port.secondary;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;

public interface RouteLogMissedServicePort {
    void logRouteLogMissedDelivery(DeliveryMissed deliveryMissed);

    void logDepotCodeMissedDelivery(DeliveryMissed deliveryMissed);
}
