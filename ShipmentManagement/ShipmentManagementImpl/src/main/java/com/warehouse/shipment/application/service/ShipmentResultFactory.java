package com.warehouse.shipment.application.service;

import com.warehouse.shipment.domain.vo.ShipmentListEntry;
import com.warehouse.shipment.application.port.primary.result.ShipmentRouteLog;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;
import com.warehouse.shipment.application.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.exception.ReturnDetailsUnavailableException;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.RouteLogRecord;
import com.warehouse.returning.api.dto.ReturnDetailsDto;

public class ShipmentResultFactory {

    private final DepartmentServicePort departmentServicePort;

    private final RouteLogService routeLogService;

    private final ReturningServicePort returningServicePort;

    public ShipmentResultFactory(final DepartmentServicePort departmentServicePort,
                                 final RouteLogService routeLogService,
                                 final ReturningServicePort returningServicePort) {
        this.departmentServicePort = departmentServicePort;
        this.routeLogService = routeLogService;
        this.returningServicePort = returningServicePort;
    }

    public ShipmentResult create(final Shipment shipment) {
        return new ShipmentResult(shipment.snapshot());
    }

    public ShipmentResult create(final ShipmentListEntry entry) {
        return new ShipmentResult(entry.shipment().snapshot(), entry.deliveryDetails());
    }

    public ShipmentRouteLog createControlCenter(final Shipment shipment) {
        final RouteLogRecord routeLog = this.routeLogService.findByShipmentId(shipment.getShipmentId()).orElse(null);
        try {
            final ReturnDetailsDto returnDetails = this.returningServicePort
                    .findReturnByShipmentId(shipment.getShipmentId()).orElse(null);
            return new ShipmentRouteLog(create(shipment), routeLog, returnDetails, false);
        } catch (final ReturnDetailsUnavailableException exception) {
            return new ShipmentRouteLog(create(shipment), routeLog, null, true);
        }
    }
}
