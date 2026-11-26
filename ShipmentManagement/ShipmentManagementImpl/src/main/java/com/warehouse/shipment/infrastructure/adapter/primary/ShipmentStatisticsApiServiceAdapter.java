package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.shipment.application.port.primary.ShipmentStatisticsPort;
import com.warehouse.shipment.infrastructure.ShipmentStatisticsApiService;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentStatisticsApiMapper;
import com.warehouse.shipment.infrastructure.dto.DailyShipmentCountDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentStatisticsDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ShipmentStatisticsApiServiceAdapter implements ShipmentStatisticsApiService {

    private final ShipmentStatisticsPort shipmentStatisticsPort;

    private final ShipmentStatisticsApiMapper shipmentStatisticsApiMapper;

    public ShipmentStatisticsApiServiceAdapter(final ShipmentStatisticsPort shipmentStatisticsPort,
                                               final ShipmentStatisticsApiMapper shipmentStatisticsApiMapper) {
        this.shipmentStatisticsPort = shipmentStatisticsPort;
        this.shipmentStatisticsApiMapper = shipmentStatisticsApiMapper;
    }

    @Override
    public ShipmentStatisticsDto getShipmentStatistics(final LocalDate dateFrom, final LocalDate dateTo) {
        return this.shipmentStatisticsApiMapper.map(this.shipmentStatisticsPort.statistics(dateFrom, dateTo));
    }

    @Override
    public List<DailyShipmentCountDto> getDailyShipmentCounts(final LocalDate dateFrom, final LocalDate dateTo) {
        return this.shipmentStatisticsPort.dailyCounts(dateFrom, dateTo).stream()
                .map(this.shipmentStatisticsApiMapper::map)
                .toList();
    }

    @Override
    public Optional<LocalDate> findFirstRegistrationDate() {
        return this.shipmentStatisticsPort.firstRegistrationDate();
    }
}
