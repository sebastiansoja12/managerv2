package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.logisticsdelivery.application.port.primary.command.SearchDeliveriesCommand;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryReadPage;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;

import java.time.LocalDate;

public interface DeliveryReadModelPort {

    void sync(final DeliverySnapshot delivery);

    int syncReadModels(final LocalDate dateFrom, final LocalDate dateTo);

    DeliveryReadPage search(final SearchDeliveriesCommand command);
}
