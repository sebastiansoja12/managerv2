package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.logisticsdelivery.api.event.snapshot.DeliveryEventData;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;

public interface DeliveryEventDataMapperPort {

    DeliveryEventData map(final DeliverySnapshot delivery);
}
