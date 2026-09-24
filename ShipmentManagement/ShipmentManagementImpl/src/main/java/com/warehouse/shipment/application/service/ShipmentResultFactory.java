package com.warehouse.shipment.application.service;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.application.port.primary.result.ShipmentRouteLog;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;
import com.warehouse.shipment.application.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.model.Shipment;
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
        return new ShipmentResult(shipment.snapshot(), resolveDepartmentCode(shipment));
    }

    public ShipmentRouteLog createControlCenter(final Shipment shipment) {
        final ReturnDetailsDto returnDetails = this.returningServicePort.findReturnByShipmentId(shipment.getShipmentId()).orElse(null);
        return new ShipmentRouteLog(
                create(shipment),
                this.routeLogService.findByShipmentId(shipment.getShipmentId()).orElse(null),
                returnDetails);
    }

    private DepartmentCode resolveDepartmentCode(final Shipment shipment) {
        return this.departmentServicePort.getDepartmentCode(shipment.getTargetDepartmentId());
    }
}
