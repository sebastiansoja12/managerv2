package com.warehouse.deliverynetwork.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.deliverynetwork.domain.model.DeliveryNetwork;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity.DeliveryNetworkConnectionEntity;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity.DeliveryNetworkConnectionId;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity.DeliveryNetworkEntity;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeliveryNetworkPersistenceMapperTest {

    private final DeliveryNetworkPersistenceMapper persistenceMapper = new DeliveryNetworkPersistenceMapper();

    @Test
    void shouldMapNetworkToPersistenceEntityAndBack() {
        final OperatorId operatorId = OperatorId.of(10L);
        final DepartmentConnection connection = new DepartmentConnection(
                new DepartmentId(1L), new DepartmentId(2L));
        final DeliveryNetwork deliveryNetwork = new DeliveryNetwork(operatorId, Set.of(connection));

        final DeliveryNetworkEntity entity = this.persistenceMapper.toEntity(deliveryNetwork);
        entity.assignOperator(operatorId);
        final DeliveryNetwork mappedNetwork = this.persistenceMapper.toModel(entity);
        final DeliveryNetworkConnectionEntity connectionEntity = entity.getConnections().iterator().next();

        assertEquals(operatorId, mappedNetwork.operatorId());
        assertEquals(Set.of(connection), mappedNetwork.connections());
        assertNotNull(entity.getDeliveryNetworkId());
        assertNotNull(connectionEntity.getConnectionId());
        assertEquals(entity.getDeliveryNetworkId(), connectionEntity.getDeliveryNetworkId());
        assertEquals(connection.firstDepartmentId(), connectionEntity.getFirstDepartmentId());
        assertEquals(connection.secondDepartmentId(), connectionEntity.getSecondDepartmentId());
    }

    @Test
    void shouldKeepPersistenceIdentityOfUnchangedConnectionDuringUpdate() {
        final OperatorId operatorId = OperatorId.of(10L);
        final DepartmentConnection unchangedConnection = connection(1L, 2L);
        final DeliveryNetworkEntity entity = this.persistenceMapper.toEntity(
                new DeliveryNetwork(operatorId, Set.of(unchangedConnection)));
        final DeliveryNetworkConnectionId unchangedConnectionId = entity.getConnections()
                .iterator()
                .next()
                .getConnectionId();

        this.persistenceMapper.updateEntity(entity, new DeliveryNetwork(
                operatorId,
                Set.of(unchangedConnection, connection(2L, 3L))));

        assertEquals(2, entity.getConnections().size());
        assertEquals(unchangedConnectionId, entity.getConnections()
                .stream()
                .filter(connectionEntity -> connectionEntity.getFirstDepartmentId().equals(new DepartmentId(1L)))
                .filter(connectionEntity -> connectionEntity.getSecondDepartmentId().equals(new DepartmentId(2L)))
                .findFirst()
                .orElseThrow()
                .getConnectionId());
    }

    private static DepartmentConnection connection(
            final Long firstDepartmentId,
            final Long secondDepartmentId) {
        return new DepartmentConnection(
                new DepartmentId(firstDepartmentId),
                new DepartmentId(secondDepartmentId));
    }
}
