package com.warehouse.deliverynetwork.application.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliverynetwork.application.exception.ShipmentDestinationDepartmentNotAssignedException;
import com.warehouse.deliverynetwork.application.exception.ShipmentOriginDepartmentNotAssignedException;
import com.warehouse.deliverynetwork.application.port.primary.result.ShipmentDeliveryRouteResult;
import com.warehouse.deliverynetwork.domain.model.DeliveryNetwork;
import com.warehouse.deliverynetwork.domain.service.DeliveryPathFinder;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;
import com.warehouse.deliverynetwork.domain.vo.DeliveryRoute;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import com.warehouse.deliverynetwork.domain.vo.ShipmentDepartments;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ShipmentDeliveryRouteFactory {

    private final DeliveryPathFinder deliveryPathFinder;

    public ShipmentDeliveryRouteFactory(final DeliveryPathFinder deliveryPathFinder) {
        this.deliveryPathFinder = deliveryPathFinder;
    }

    public ShipmentDeliveryRouteResult create(final TrackingNumber trackingNumber,
                                              final ShipmentDepartments shipmentDepartments,
                                              final Supplier<DeliveryNetwork> deliveryNetwork,
                                              final Supplier<List<DepartmentNode>> currentDepartments) {
        final DepartmentId originDepartmentId = shipmentDepartments.originDepartment()
                .orElseThrow(() -> new ShipmentOriginDepartmentNotAssignedException(trackingNumber));
        final DepartmentId destinationDepartmentId = shipmentDepartments.destinationDepartment()
                .orElseThrow(() -> new ShipmentDestinationDepartmentNotAssignedException(trackingNumber));
        final List<DepartmentNode> departments = currentDepartments.get();
        final DeliveryPath deliveryPath = this.deliveryPathFinder.findShortestPath(deliveryNetwork.get(), departments,
                originDepartmentId, destinationDepartmentId);
        final Map<DepartmentId, DepartmentNode> departmentsById = departments.stream()
                .collect(Collectors.toMap(DepartmentNode::departmentId, Function.identity()));
        final DeliveryRoute route = new DeliveryRoute(deliveryPath.departmentIds()
                .stream()
                .map(departmentsById::get)
                .toList());

        return new ShipmentDeliveryRouteResult(shipmentDepartments.shipmentId(), trackingNumber,
                route.departments(), route.distanceInKilometers().orElse(null));
    }
}
