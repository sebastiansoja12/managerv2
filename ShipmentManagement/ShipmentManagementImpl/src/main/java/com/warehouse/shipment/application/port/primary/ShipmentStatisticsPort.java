package com.warehouse.shipment.application.port.primary;

import com.warehouse.shipment.application.port.primary.result.DailyShipmentCount;
import com.warehouse.shipment.application.port.primary.result.ShipmentStatistics;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShipmentStatisticsPort {

    ShipmentStatistics statistics(final LocalDate dateFrom, final LocalDate dateTo);

    List<DailyShipmentCount> dailyCounts(final LocalDate dateFrom, final LocalDate dateTo);

    Optional<LocalDate> firstRegistrationDate();
}
