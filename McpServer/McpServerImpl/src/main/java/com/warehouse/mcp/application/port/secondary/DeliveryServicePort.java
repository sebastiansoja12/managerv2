package com.warehouse.mcp.application.port.secondary;

import java.util.List;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.mcp.domain.vo.DeliveryConditions;
import com.warehouse.mcp.domain.vo.DeliveryDetails;
import com.warehouse.mcp.domain.vo.DeliveryRoute;

public interface DeliveryServicePort {
    List<DeliveryDetails> findDeliveries(final DepartmentId departmentId, final SupplierId supplierId, final int limit);
    DeliveryDetails findDelivery(final DeliveryId deliveryId);
    DeliveryDetails findDeliveryByShipment(final ShipmentId shipmentId);
    DeliveryDetails assignCourier(final DeliveryId deliveryId, final SupplierId supplierId, final VehicleId vehicleId);
    DeliveryDetails unassignCourier(final DeliveryId deliveryId);
    DeliveryDetails changeRoute(final DeliveryId deliveryId, final DeliveryRoute route);
    DeliveryDetails changeConditions(final DeliveryId deliveryId, final DeliveryConditions conditions);
    DeliveryDetails commentStep(final DeliveryId deliveryId, final int stepNumber, final String comment);
    DeliveryDetails correctStepStatus(final DeliveryId deliveryId, final int stepNumber, final String deliveryStatus);
    void cancel(final DeliveryId deliveryId);
}
