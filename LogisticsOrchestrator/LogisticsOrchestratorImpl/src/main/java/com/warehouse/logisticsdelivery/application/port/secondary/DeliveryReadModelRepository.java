package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.logisticsdelivery.application.port.primary.command.SearchDeliveriesCommand;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryReadPage;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;

public interface DeliveryReadModelRepository {

    void sync(final DeliverySnapshot delivery);

    DeliveryReadPage search(final SearchDeliveriesCommand command);
}
