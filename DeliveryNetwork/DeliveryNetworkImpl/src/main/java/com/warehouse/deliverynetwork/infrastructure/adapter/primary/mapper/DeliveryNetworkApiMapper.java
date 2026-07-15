package com.warehouse.deliverynetwork.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.api.dto.DepartmentConnectionDto;
import com.warehouse.deliverynetwork.api.dto.DepartmentIdDto;
import com.warehouse.deliverynetwork.api.dto.DeliveryNetworkDto;
import com.warehouse.deliverynetwork.api.dto.DeliveryPathDto;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkResult;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;

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

    public DepartmentId toModel(final DepartmentIdDto departmentId) {
        return new DepartmentId(departmentId.value());
    }

    private DepartmentConnectionDto toDto(final DepartmentConnection connection) {
        return new DepartmentConnectionDto(
                toDto(connection.firstDepartmentId()),
                toDto(connection.secondDepartmentId()));
    }

    private DepartmentIdDto toDto(final DepartmentId departmentId) {
        return new DepartmentIdDto(departmentId.getValue());
    }
}
