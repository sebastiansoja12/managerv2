package com.warehouse.deliverynetwork.domain.service;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;
import com.warehouse.deliverynetwork.domain.exception.DeliveryPathNotFoundException;
import com.warehouse.deliverynetwork.domain.model.DeliveryNetwork;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.domain.vo.DepartmentLocation;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
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
        final List<DepartmentNode> departments = List.of(branch(1L), branch(2L));

        final DeliveryPath forwardPath = this.deliveryPathFinder.findShortestPath(
                deliveryNetwork, departments, departmentId(1L), departmentId(2L));
        final DeliveryPath reversePath = this.deliveryPathFinder.findShortestPath(
                deliveryNetwork, departments, departmentId(2L), departmentId(1L));

        assertEquals(List.of(departmentId(1L), departmentId(2L)), forwardPath.departmentIds());
        assertEquals(List.of(departmentId(2L), departmentId(1L)), reversePath.departmentIds());
    }

    @Test
    void shouldPreferDirectConnectionOverSortingFacility() {
        final DeliveryNetwork deliveryNetwork = network(
                connection(1L, 2L),
                connection(1L, 10L),
                connection(10L, 2L));
        final List<DepartmentNode> departments = List.of(branch(1L), branch(2L), sortingFacility(10L));

        final DeliveryPath deliveryPath = this.deliveryPathFinder.findShortestPath(
                deliveryNetwork, departments, departmentId(1L), departmentId(2L));

        assertEquals(List.of(departmentId(1L), departmentId(2L)), deliveryPath.departmentIds());
    }

    @Test
    void shouldRouteThroughSortingFacilityConnectedWithTargetDepartment() {
        final DeliveryNetwork deliveryNetwork = network(
                connection(1L, 10L),
                connection(1L, 11L),
                connection(11L, 2L));
        final List<DepartmentNode> departments = List.of(
                branch(1L), branch(2L), sortingFacility(10L), sortingFacility(11L));

        final DeliveryPath deliveryPath = this.deliveryPathFinder.findShortestPath(
                deliveryNetwork, departments, departmentId(1L), departmentId(2L));

        assertEquals(List.of(departmentId(1L), departmentId(11L), departmentId(2L)), deliveryPath.departmentIds());
    }

    @Test
    void shouldRouteThroughConnectedSortingFacilities() {
        final DeliveryNetwork deliveryNetwork = network(
                connection(1L, 10L),
                connection(10L, 11L),
                connection(11L, 2L));
        final List<DepartmentNode> departments = List.of(
                branch(1L), branch(2L), sortingFacility(10L), sortingFacility(11L));

        final DeliveryPath deliveryPath = this.deliveryPathFinder.findShortestPath(
                deliveryNetwork, departments, departmentId(1L), departmentId(2L));

        assertEquals(List.of(departmentId(1L), departmentId(10L), departmentId(11L), departmentId(2L)),
                deliveryPath.departmentIds());
    }

    @Test
    void shouldPreferSortingFacilitiesOverBranchTransfer() {
        final DeliveryNetwork deliveryNetwork = network(
                connection(1L, 3L),
                connection(3L, 2L),
                connection(1L, 10L),
                connection(10L, 11L),
                connection(11L, 2L));
        final List<DepartmentNode> departments = List.of(
                branch(1L), branch(2L), branch(3L), sortingFacility(10L), sortingFacility(11L));

        final DeliveryPath deliveryPath = this.deliveryPathFinder.findShortestPath(
                deliveryNetwork, departments, departmentId(1L), departmentId(2L));

        assertEquals(List.of(departmentId(1L), departmentId(10L), departmentId(11L), departmentId(2L)),
                deliveryPath.departmentIds());
    }

    @Test
    void shouldContinueThroughBranchWhenSortingFacilityHasNoFurtherConnection() {
        final DeliveryNetwork deliveryNetwork = network(
                connection(1L, 10L),
                connection(10L, 3L),
                connection(3L, 11L),
                connection(11L, 2L));
        final List<DepartmentNode> departments = List.of(
                branch(1L), branch(2L), branch(3L), sortingFacility(10L), sortingFacility(11L));

        final DeliveryPath deliveryPath = this.deliveryPathFinder.findShortestPath(
                deliveryNetwork, departments, departmentId(1L), departmentId(2L));

        assertEquals(
                List.of(departmentId(1L), departmentId(10L), departmentId(3L), departmentId(11L), departmentId(2L)),
                deliveryPath.departmentIds());
    }

    @Test
    void shouldSkipArchivedDepartments() {
        final DeliveryNetwork deliveryNetwork = network(
                connection(1L, 10L),
                connection(10L, 3L),
                connection(3L, 2L));
        final List<DepartmentNode> departments = List.of(
                branch(1L), branch(2L), sortingFacility(10L),
                new DepartmentNode(departmentId(3L), new DepartmentCode("D3"), DepartmentType.BRANCH,
                        DepartmentStatus.ARCHIVED));

        assertThrows(
                DeliveryPathNotFoundException.class,
                () -> this.deliveryPathFinder.findShortestPath(
                        deliveryNetwork, departments, departmentId(1L), departmentId(2L)));
    }

    @Test
    void shouldChooseClosestSortingFacilityWhenTransferCountIsEqual() {
        final DeliveryNetwork deliveryNetwork = network(
                connection(1L, 10L),
                connection(10L, 2L),
                connection(1L, 11L),
                connection(11L, 2L));
        final List<DepartmentNode> departments = List.of(
                department(1L, DepartmentType.BRANCH, new DepartmentLocation(52.23, 21.01)),
                department(2L, DepartmentType.BRANCH, new DepartmentLocation(50.06, 19.94)),
                department(10L, DepartmentType.SORTING_FACILITY, new DepartmentLocation(54.35, 18.65)),
                department(11L, DepartmentType.SORTING_FACILITY, new DepartmentLocation(51.10, 20.50)));

        final DeliveryPath deliveryPath = this.deliveryPathFinder.findShortestPath(
                deliveryNetwork, departments, departmentId(1L), departmentId(2L));

        assertEquals(List.of(departmentId(1L), departmentId(11L), departmentId(2L)), deliveryPath.departmentIds());
    }

    @Test
    void shouldReturnSingleDepartmentWhenSourceEqualsTarget() {
        final DeliveryNetwork deliveryNetwork = network();

        final DeliveryPath deliveryPath = this.deliveryPathFinder.findShortestPath(
                deliveryNetwork, List.of(branch(1L)), departmentId(1L), departmentId(1L));

        assertEquals(List.of(departmentId(1L)), deliveryPath.departmentIds());
    }

    @Test
    void shouldRejectDisconnectedDepartments() {
        final DeliveryNetwork deliveryNetwork = network(connection(1L, 10L), connection(2L, 11L));
        final List<DepartmentNode> departments = List.of(
                branch(1L), branch(2L), sortingFacility(10L), sortingFacility(11L));

        assertThrows(
                DeliveryPathNotFoundException.class,
                () -> this.deliveryPathFinder.findShortestPath(
                        deliveryNetwork, departments, departmentId(1L), departmentId(2L)));
    }

    private static DeliveryNetwork network(final DepartmentConnection... connections) {
        return new DeliveryNetwork(OperatorId.of(10L), Set.of(connections));
    }

    private static DepartmentConnection connection(final Long firstDepartmentId, final Long secondDepartmentId) {
        return new DepartmentConnection(departmentId(firstDepartmentId), departmentId(secondDepartmentId));
    }

    private static DepartmentNode branch(final Long departmentId) {
        return department(departmentId, DepartmentType.BRANCH, null);
    }

    private static DepartmentNode sortingFacility(final Long departmentId) {
        return department(departmentId, DepartmentType.SORTING_FACILITY, null);
    }

    private static DepartmentNode department(final Long departmentId,
                                             final DepartmentType departmentType,
                                             final DepartmentLocation location) {
        return new DepartmentNode(
                departmentId(departmentId),
                new DepartmentCode("D" + departmentId),
                departmentType,
                DepartmentStatus.ACTIVE,
                location);
    }

    private static DepartmentId departmentId(final Long value) {
        return new DepartmentId(value);
    }
}
