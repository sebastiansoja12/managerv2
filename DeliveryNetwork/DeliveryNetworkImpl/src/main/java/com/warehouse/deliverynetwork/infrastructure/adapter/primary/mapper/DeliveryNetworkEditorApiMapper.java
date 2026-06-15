package com.warehouse.deliverynetwork.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.application.port.primary.command.DepartmentConnectionCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkCommand;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkResult;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.DepartmentConnectionApi;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.DepartmentIdApi;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.DeliveryNetworkApiResponse;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.ReplaceDeliveryNetworkApiRequest;

import java.util.Comparator;
import java.util.List;

public class DeliveryNetworkEditorApiMapper {

    public ReplaceDeliveryNetworkCommand toCommand(final ReplaceDeliveryNetworkApiRequest request) {
        final List<DepartmentConnectionCommand> connections = request.connections()
                .stream()
                .map(this::toCommand)
                .toList();
        return new ReplaceDeliveryNetworkCommand(connections);
    }

    public DeliveryNetworkApiResponse toResponse(final DeliveryNetworkResult deliveryNetwork) {
        final List<DepartmentConnectionApi> connections = deliveryNetwork.connections()
                .stream()
                .sorted(Comparator
                        .comparing((DepartmentConnection connection) ->
                                connection.firstDepartmentId().getValue())
                        .thenComparing(connection -> connection.secondDepartmentId().getValue()))
                .map(this::toApi)
                .toList();
        return new DeliveryNetworkApiResponse(connections);
    }

    private DepartmentConnectionCommand toCommand(final DepartmentConnectionApi connection) {
        return new DepartmentConnectionCommand(
                toModel(connection.firstDepartmentId()),
                toModel(connection.secondDepartmentId()));
    }

    private DepartmentId toModel(final DepartmentIdApi departmentId) {
        return new DepartmentId(Long.valueOf(departmentId.value()));
    }

    private DepartmentConnectionApi toApi(final DepartmentConnection connection) {
        return new DepartmentConnectionApi(
                toApi(connection.firstDepartmentId()),
                toApi(connection.secondDepartmentId()));
    }

    private DepartmentIdApi toApi(final DepartmentId departmentId) {
        return new DepartmentIdApi(String.valueOf(departmentId.getValue()));
    }
}
