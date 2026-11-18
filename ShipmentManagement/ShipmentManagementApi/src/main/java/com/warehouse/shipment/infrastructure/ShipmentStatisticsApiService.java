package com.warehouse.shipment.infrastructure;

import com.warehouse.shipment.infrastructure.dto.DailyShipmentCountDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentStatisticsDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShipmentStatisticsApiService {

    ShipmentStatisticsDto getShipmentStatistics(final LocalDate dateFrom, final LocalDate dateTo);

    List<DailyShipmentCountDto> getDailyShipmentCounts(final LocalDate dateFrom, final LocalDate dateTo);

    Optional<LocalDate> findFirstRegistrationDate();
}
