package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.application.port.primary.result.DailyShipmentCount;
import com.warehouse.shipment.application.port.primary.result.ShipmentDeliveryFigures;
import com.warehouse.shipment.application.port.primary.result.ShipmentRegistrationFigures;
import com.warehouse.shipment.application.port.primary.result.ShipmentStatistics;
import com.warehouse.shipment.infrastructure.dto.DailyShipmentCountDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentDeliveryFiguresDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRegistrationFiguresDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentStatisticsDto;

public class ShipmentStatisticsApiMapper {

    public ShipmentStatisticsDto map(final ShipmentStatistics statistics) {
        return new ShipmentStatisticsDto(
                statistics.registrations().stream().map(this::map).toList(),
                statistics.deliveries().stream().map(this::map).toList());
    }

    public ShipmentRegistrationFiguresDto map(final ShipmentRegistrationFigures figures) {
        return new ShipmentRegistrationFiguresDto(figures.originDepartmentId(), figures.currency(),
                figures.registeredShipments(), figures.canceledShipments(), figures.registeredValue());
    }

    public ShipmentDeliveryFiguresDto map(final ShipmentDeliveryFigures figures) {
        return new ShipmentDeliveryFiguresDto(figures.originDepartmentId(), figures.currency(),
                figures.deliveredShipments(), figures.deliveredValue());
    }

    public DailyShipmentCountDto map(final DailyShipmentCount dailyShipmentCount) {
        return new DailyShipmentCountDto(dailyShipmentCount.originDepartmentId(), dailyShipmentCount.day(),
                dailyShipmentCount.registeredShipments());
    }
}
