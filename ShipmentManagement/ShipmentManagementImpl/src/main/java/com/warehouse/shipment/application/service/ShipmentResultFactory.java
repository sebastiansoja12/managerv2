package com.warehouse.shipment.application.service;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.application.port.primary.result.ShipmentControlCenterResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;
import com.warehouse.shipment.application.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentReturnDetails;

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

    public ShipmentControlCenterResult createControlCenter(final Shipment shipment) {
        final ShipmentReturnDetails returnDetails = ShipmentStatus.RETURN.equals(shipment.getShipmentStatus())
                ? this.returningServicePort.findReturnByShipmentId(shipment.getShipmentId()).orElse(null)
                : null;
        return new ShipmentControlCenterResult(
                create(shipment),
                this.routeLogService.findByShipmentId(shipment.getShipmentId()).orElse(null),
                returnDetails);
    }

    private DepartmentCode resolveDepartmentCode(final Shipment shipment) {
        return this.departmentServicePort.getDepartmentCode(shipment.getTargetDepartmentId());
    }
}
