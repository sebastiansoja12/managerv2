package com.warehouse.deliverynetwork.infrastructure.adapter.secondary.mapper;

import com.warehouse.deliverynetwork.domain.model.DeliveryNetwork;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity.DeliveryNetworkConnectionEntity;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity.DeliveryNetworkEntity;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity.DeliveryNetworkId;

import java.util.Set;
import java.util.stream.Collectors;

public class DeliveryNetworkPersistenceMapper {

    public DeliveryNetwork toModel(final DeliveryNetworkEntity entity) {
        final Set<DepartmentConnection> connections = entity.getConnections()
                .stream()
                .map(this::toModel)
                .collect(Collectors.toSet());
        return new DeliveryNetwork(entity.operatorId(), connections);
    }

    public DeliveryNetworkEntity toEntity(final DeliveryNetwork deliveryNetwork) {
        final DeliveryNetworkId deliveryNetworkId = DeliveryNetworkId.generate();
        return new DeliveryNetworkEntity(
                deliveryNetworkId,
                toEntities(deliveryNetworkId, deliveryNetwork.connections()));
    }

    public void updateEntity(final DeliveryNetworkEntity entity, final DeliveryNetwork deliveryNetwork) {
        entity.replaceConnections(toEntities(entity.getDeliveryNetworkId(), deliveryNetwork.connections()));
    }

    private Set<DeliveryNetworkConnectionEntity> toEntities(
            final DeliveryNetworkId deliveryNetworkId,
            final Set<DepartmentConnection> connections) {
        return connections.stream()
                .map(connection -> toEntity(deliveryNetworkId, connection))
                .collect(Collectors.toSet());
    }

    private DeliveryNetworkConnectionEntity toEntity(
            final DeliveryNetworkId deliveryNetworkId,
            final DepartmentConnection connection) {
        return new DeliveryNetworkConnectionEntity(
                deliveryNetworkId,
                connection.firstDepartmentId(),
                connection.secondDepartmentId());
    }

    private DepartmentConnection toModel(final DeliveryNetworkConnectionEntity entity) {
        return new DepartmentConnection(
                entity.getFirstDepartmentId(),
                entity.getSecondDepartmentId());
    }
}
