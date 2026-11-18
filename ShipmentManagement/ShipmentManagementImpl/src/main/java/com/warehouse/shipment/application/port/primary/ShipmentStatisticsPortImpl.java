package com.warehouse.shipment.application.port.primary;

import com.warehouse.shipment.application.port.primary.result.DailyShipmentCount;
import com.warehouse.shipment.application.port.primary.result.ShipmentStatistics;
import com.warehouse.shipment.application.port.secondary.ShipmentStatisticsRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ShipmentStatisticsPortImpl implements ShipmentStatisticsPort {

    private final ShipmentStatisticsRepository shipmentStatisticsRepository;

    public ShipmentStatisticsPortImpl(final ShipmentStatisticsRepository shipmentStatisticsRepository) {
        this.shipmentStatisticsRepository = shipmentStatisticsRepository;
    }

    @Override
    public ShipmentStatistics statistics(final LocalDate dateFrom, final LocalDate dateTo) {
        final LocalDateTime rangeStart = dateFrom.atStartOfDay();
        final LocalDateTime rangeEnd = dateTo.plusDays(1).atStartOfDay();
        return new ShipmentStatistics(
                this.shipmentStatisticsRepository.registrationFigures(rangeStart, rangeEnd),
                this.shipmentStatisticsRepository.deliveryFigures(rangeStart, rangeEnd));
    }

    @Override
    public List<DailyShipmentCount> dailyCounts(final LocalDate dateFrom, final LocalDate dateTo) {
        return this.shipmentStatisticsRepository.dailyRegistrations(dateFrom.atStartOfDay(),
                dateTo.plusDays(1).atStartOfDay());
    }

    @Override
    public Optional<LocalDate> firstRegistrationDate() {
        return this.shipmentStatisticsRepository.firstRegistration().map(LocalDateTime::toLocalDate);
    }
}
