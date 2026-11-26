package com.warehouse.analytics.infrastructure.adapter.secondary;

import com.warehouse.analytics.application.port.secondary.ShipmentStatisticsServicePort;
import com.warehouse.analytics.domain.vo.AnalyticsPeriod;
import com.warehouse.analytics.domain.vo.DailyRegistrations;
import com.warehouse.analytics.domain.vo.DeliveryFigures;
import com.warehouse.analytics.domain.vo.RegistrationFigures;
import com.warehouse.analytics.domain.vo.ShipmentFigures;
import com.warehouse.shipment.infrastructure.ShipmentStatisticsApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentStatisticsDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ShipmentStatisticsServiceAdapter implements ShipmentStatisticsServicePort {

    private final ShipmentStatisticsApiService shipmentStatisticsApiService;

    public ShipmentStatisticsServiceAdapter(final ShipmentStatisticsApiService shipmentStatisticsApiService) {
        this.shipmentStatisticsApiService = shipmentStatisticsApiService;
    }

    @Override
    public ShipmentFigures shipmentFigures(final AnalyticsPeriod period) {
        final ShipmentStatisticsDto statistics = this.shipmentStatisticsApiService.getShipmentStatistics(
                period.dateFrom(), period.dateTo());
        return new ShipmentFigures(
                statistics.registrations().stream()
                        .map(figures -> new RegistrationFigures(figures.originDepartmentId(), figures.currency(),
                                figures.registeredShipments(), figures.canceledShipments(),
                                figures.registeredValue()))
                        .toList(),
                statistics.deliveries().stream()
                        .map(figures -> new DeliveryFigures(figures.originDepartmentId(), figures.currency(),
                                figures.deliveredShipments(), figures.deliveredValue()))
                        .toList());
    }

    @Override
    public List<DailyRegistrations> dailyRegistrations(final LocalDate dateFrom, final LocalDate dateTo) {
        return this.shipmentStatisticsApiService.getDailyShipmentCounts(dateFrom, dateTo).stream()
                .map(count -> new DailyRegistrations(count.originDepartmentId(), count.day(),
                        count.registeredShipments()))
                .toList();
    }

    @Override
    public Optional<LocalDate> firstRegistrationDate() {
        return this.shipmentStatisticsApiService.findFirstRegistrationDate();
    }
}
