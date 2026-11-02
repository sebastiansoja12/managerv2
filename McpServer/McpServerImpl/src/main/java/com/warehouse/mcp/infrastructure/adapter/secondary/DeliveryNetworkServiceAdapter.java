package com.warehouse.mcp.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliverynetwork.api.DeliveryNetworkApiService;
import com.warehouse.deliverynetwork.api.dto.RouteDepartmentDto;
import com.warehouse.deliverynetwork.api.dto.ShipmentDeliveryRouteDto;
import com.warehouse.mcp.application.port.secondary.DeliveryNetworkServicePort;
import com.warehouse.mcp.domain.vo.DepartmentEntry;
import com.warehouse.mcp.domain.vo.ShipmentRoute;

public class DeliveryNetworkServiceAdapter implements DeliveryNetworkServicePort {

    private final DeliveryNetworkApiService deliveryNetworkApiService;

    public DeliveryNetworkServiceAdapter(final DeliveryNetworkApiService deliveryNetworkApiService) {
        this.deliveryNetworkApiService = deliveryNetworkApiService;
    }

    @Override
    public ShipmentRoute findShipmentRoute(final TrackingNumber trackingNumber) {
        final ShipmentDeliveryRouteDto route = this.deliveryNetworkApiService.findShipmentDeliveryRoute(trackingNumber);
        return new ShipmentRoute(route.departments().stream().map(this::map).toList(), route.distanceInKilometers());
    }

    private DepartmentEntry map(final RouteDepartmentDto department) {
        return new DepartmentEntry(
                department.departmentId() != null ? new DepartmentId(department.departmentId().value()) : null,
                department.departmentCode(), department.departmentType(), department.status());
    }
}
