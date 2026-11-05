package com.warehouse.deliverynetwork.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.api.dto.DepartmentConnectionDto;
import com.warehouse.deliverynetwork.api.dto.DepartmentIdDto;
import com.warehouse.deliverynetwork.api.dto.DeliveryNetworkDto;
import com.warehouse.deliverynetwork.api.dto.DeliveryPathDto;
import com.warehouse.deliverynetwork.api.dto.RouteDepartmentDto;
import com.warehouse.deliverynetwork.api.dto.ShipmentDeliveryRouteDto;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkResult;
import com.warehouse.deliverynetwork.application.port.primary.result.ShipmentDeliveryRouteResult;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;

import java.util.Comparator;
import java.util.List;

public class DeliveryNetworkApiMapper {

    public DeliveryNetworkDto toDto(final DeliveryNetworkResult deliveryNetwork) {
        final List<DepartmentConnectionDto> connections = deliveryNetwork.connections()
                .stream()
                .sorted(Comparator
                        .comparing((DepartmentConnection connection) ->
                                connection.firstDepartmentId().getValue())
                        .thenComparing(connection -> connection.secondDepartmentId().getValue()))
                .map(this::toDto)
                .toList();
        return new DeliveryNetworkDto(connections);
    }

    public DeliveryPathDto toDto(final DeliveryPath deliveryPath) {
        return new DeliveryPathDto(deliveryPath.departmentIds()
                .stream()
                .map(this::toDto)
                .toList());
    }

    public ShipmentDeliveryRouteDto toDto(final ShipmentDeliveryRouteResult shipmentDeliveryRoute) {
        return new ShipmentDeliveryRouteDto(shipmentDeliveryRoute.departments()
                .stream()
                .map(this::toDto)
                .toList(), shipmentDeliveryRoute.distanceInKilometers());
    }

    public DepartmentId toModel(final DepartmentIdDto departmentId) {
        return new DepartmentId(departmentId.value());
    }

    private DepartmentConnectionDto toDto(final DepartmentConnection connection) {
        return new DepartmentConnectionDto(
                toDto(connection.firstDepartmentId()),
                toDto(connection.secondDepartmentId()),
                connection.bidirectional());
    }

    private RouteDepartmentDto toDto(final DepartmentNode department) {
        return new RouteDepartmentDto(
                toDto(department.departmentId()),
                department.departmentCode(),
                department.departmentType().name(),
                department.status() != null ? department.status().name() : null);
    }

    private DepartmentIdDto toDto(final DepartmentId departmentId) {
        return new DepartmentIdDto(departmentId.getValue());
    }
}
