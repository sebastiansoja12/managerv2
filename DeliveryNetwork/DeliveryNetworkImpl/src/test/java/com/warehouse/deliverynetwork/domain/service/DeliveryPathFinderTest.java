package com.warehouse.deliverynetwork.domain.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.deliverynetwork.domain.exception.DeliveryPathNotFoundException;
import com.warehouse.deliverynetwork.domain.model.DeliveryNetwork;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeliveryPathFinderTest {

    private final DeliveryPathFinder deliveryPathFinder = new DeliveryPathFinder();

    @Test
    void shouldFindDirectPathInBothDirections() {
        final DeliveryNetwork deliveryNetwork = network(connection(1L, 2L));

        final DeliveryPath forwardPath = this.deliveryPathFinder.findShortestPath(
                deliveryNetwork, departmentId(1L), departmentId(2L));
        final DeliveryPath reversePath = this.deliveryPathFinder.findShortestPath(
                deliveryNetwork, departmentId(2L), departmentId(1L));

        assertEquals(List.of(departmentId(1L), departmentId(2L)), forwardPath.departmentIds());
        assertEquals(List.of(departmentId(2L), departmentId(1L)), reversePath.departmentIds());
    }

    @Test
    void shouldFindShortestIndirectPath() {
        final DeliveryNetwork deliveryNetwork = network(
                connection(1L, 2L),
                connection(2L, 3L),
                connection(1L, 4L),
                connection(4L, 5L),
                connection(5L, 3L));

        final DeliveryPath deliveryPath = this.deliveryPathFinder.findShortestPath(
                deliveryNetwork, departmentId(1L), departmentId(3L));

        assertEquals(List.of(departmentId(1L), departmentId(2L), departmentId(3L)), deliveryPath.departmentIds());
    }

    @Test
    void shouldReturnSingleDepartmentWhenSourceEqualsTarget() {
        final DeliveryNetwork deliveryNetwork = network();

        final DeliveryPath deliveryPath = this.deliveryPathFinder.findShortestPath(
                deliveryNetwork, departmentId(1L), departmentId(1L));

        assertEquals(List.of(departmentId(1L)), deliveryPath.departmentIds());
    }

    @Test
    void shouldRejectDisconnectedDepartments() {
        final DeliveryNetwork deliveryNetwork = network(connection(1L, 2L), connection(3L, 4L));

        assertThrows(
                DeliveryPathNotFoundException.class,
                () -> this.deliveryPathFinder.findShortestPath(
                        deliveryNetwork, departmentId(1L), departmentId(4L)));
    }

    private static DeliveryNetwork network(final DepartmentConnection... connections) {
        return new DeliveryNetwork(OperatorId.of(10L), Set.of(connections));
    }

    private static DepartmentConnection connection(final Long firstDepartmentId, final Long secondDepartmentId) {
        return new DepartmentConnection(departmentId(firstDepartmentId), departmentId(secondDepartmentId));
    }

    private static DepartmentId departmentId(final Long value) {
        return new DepartmentId(value);
    }
}
