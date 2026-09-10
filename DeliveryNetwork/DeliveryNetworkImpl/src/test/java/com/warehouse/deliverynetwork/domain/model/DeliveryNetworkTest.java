package com.warehouse.deliverynetwork.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;
import com.warehouse.deliverynetwork.domain.exception.DuplicateDepartmentConnectionException;
import com.warehouse.deliverynetwork.domain.exception.MissingSortingFacilityConnectionException;
import com.warehouse.deliverynetwork.domain.exception.SelfDepartmentConnectionException;
import com.warehouse.deliverynetwork.domain.exception.UnavailableDepartmentException;
import com.warehouse.deliverynetwork.domain.exception.UnknownDepartmentException;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeliveryNetworkTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(10L);

    @Test
    void shouldCanonicalizeBidirectionalConnection() {
        final DepartmentConnection kt1ToNcs = connection(1L, 2L);
        final DepartmentConnection ncsToKt1 = connection(2L, 1L);

        assertEquals(kt1ToNcs, ncsToKt1);
        assertEquals(new DepartmentId(1L), ncsToKt1.firstDepartmentId());
        assertEquals(new DepartmentId(2L), ncsToKt1.secondDepartmentId());
    }

    @Test
    void shouldRejectConnectionToTheSameDepartment() {
        assertThrows(SelfDepartmentConnectionException.class, () -> connection(1L, 1L));
    }

    @Test
    void shouldReplaceCompleteValidNetwork() {
        final DeliveryNetwork deliveryNetwork = DeliveryNetwork.empty(OPERATOR_ID);
        final DepartmentConnection kt1ToNcs = connection(1L, 2L);
        final DepartmentConnection ncsToPoz = connection(2L, 3L);

        deliveryNetwork.replaceConnections(
                List.of(kt1ToNcs, ncsToPoz),
                List.of(branch(1L, "KT1"), sortingFacility(2L, "NCS"), branch(3L, "POZ")));

        assertEquals(Set.of(kt1ToNcs, ncsToPoz), deliveryNetwork.connections());
        assertTrue(deliveryNetwork.directlyConnects(new DepartmentId(2L), new DepartmentId(1L)));
        assertFalse(deliveryNetwork.directlyConnects(new DepartmentId(1L), new DepartmentId(3L)));
    }

    @Test
    void shouldRejectDuplicateConnectionInOppositeDirection() {
        final DeliveryNetwork deliveryNetwork = DeliveryNetwork.empty(OPERATOR_ID);

        assertThrows(
                DuplicateDepartmentConnectionException.class,
                () -> deliveryNetwork.replaceConnections(
                        List.of(connection(1L, 2L), connection(2L, 1L)),
                        List.of(branch(1L, "KT1"), sortingFacility(2L, "NCS"))));
    }

    @Test
    void shouldReportAllDepartmentsWithoutSortingFacilityConnection() {
        final DeliveryNetwork deliveryNetwork = DeliveryNetwork.empty(OPERATOR_ID);

        final MissingSortingFacilityConnectionException exception = assertThrows(
                MissingSortingFacilityConnectionException.class,
                () -> deliveryNetwork.replaceConnections(
                        List.of(connection(1L, 3L)),
                        List.of(
                                branch(1L, "KT1"),
                                sortingFacility(2L, "NCS"),
                                branch(3L, "POZ"),
                                branch(4L, "GD1"))));

        assertEquals(
                List.of(new DepartmentCode("GD1"), new DepartmentCode("KT1"), new DepartmentCode("POZ")),
                exception.departmentCodes());
    }

    @Test
    void shouldNotRequireSortingFacilityToConnectToAnotherSortingFacility() {
        final DeliveryNetwork deliveryNetwork = DeliveryNetwork.empty(OPERATOR_ID);

        deliveryNetwork.replaceConnections(List.of(), List.of(sortingFacility(2L, "NCS")));

        assertTrue(deliveryNetwork.connections().isEmpty());
    }

    @Test
    void shouldRejectUnknownDepartmentInConnection() {
        final DeliveryNetwork deliveryNetwork = DeliveryNetwork.empty(OPERATOR_ID);

        assertThrows(
                UnknownDepartmentException.class,
                () -> deliveryNetwork.replaceConnections(
                        List.of(connection(1L, 99L)),
                        List.of(branch(1L, "KT1"), sortingFacility(2L, "NCS"))));
    }

    @ParameterizedTest
    @EnumSource(value = DepartmentStatus.class, names = {"ARCHIVED", "DELETED"})
    void shouldRejectUnavailableDepartmentInConnection(final DepartmentStatus status) {
        final DeliveryNetwork deliveryNetwork = DeliveryNetwork.empty(OPERATOR_ID);
        final DepartmentNode unavailableDepartment = department(1L, "KT1", DepartmentType.BRANCH, status);

        assertThrows(
                UnavailableDepartmentException.class,
                () -> deliveryNetwork.replaceConnections(
                        List.of(connection(1L, 2L)),
                        List.of(unavailableDepartment, sortingFacility(2L, "NCS"))));
    }

    @Test
    void shouldKeepExistingNetworkWhenReplacementIsInvalid() {
        final DeliveryNetwork deliveryNetwork = DeliveryNetwork.empty(OPERATOR_ID);
        final DepartmentConnection existingConnection = connection(1L, 2L);
        final List<DepartmentNode> departments = List.of(branch(1L, "KT1"), sortingFacility(2L, "NCS"));
        deliveryNetwork.replaceConnections(List.of(existingConnection), departments);

        assertThrows(
                MissingSortingFacilityConnectionException.class,
                () -> deliveryNetwork.replaceConnections(List.of(), departments));

        assertEquals(Set.of(existingConnection), deliveryNetwork.connections());
    }

    @Test
    void shouldRemoveEveryConnectionForDepartmentWithoutValidatingDirectory() {
        final DepartmentConnection firstConnection = connection(1L, 2L);
        final DepartmentConnection secondConnection = connection(2L, 3L);
        final DepartmentConnection remainingConnection = connection(3L, 4L);
        final DeliveryNetwork deliveryNetwork = new DeliveryNetwork(
                OPERATOR_ID,
                Set.of(firstConnection, secondConnection, remainingConnection));

        final boolean changed = deliveryNetwork.removeConnectionsFor(new DepartmentId(2L));

        assertTrue(changed);
        assertEquals(Set.of(remainingConnection), deliveryNetwork.connections());
    }

    private static DepartmentConnection connection(final Long firstDepartmentId, final Long secondDepartmentId) {
        return new DepartmentConnection(new DepartmentId(firstDepartmentId), new DepartmentId(secondDepartmentId));
    }

    private static DepartmentNode branch(final Long departmentId, final String departmentCode) {
        return department(departmentId, departmentCode, DepartmentType.BRANCH, DepartmentStatus.ACTIVE);
    }

    private static DepartmentNode sortingFacility(final Long departmentId, final String departmentCode) {
        return department(departmentId, departmentCode, DepartmentType.SORTING_FACILITY, DepartmentStatus.ACTIVE);
    }

    private static DepartmentNode department(
            final Long departmentId,
            final String departmentCode,
            final DepartmentType departmentType,
            final DepartmentStatus status) {
        return new DepartmentNode(
                new DepartmentId(departmentId),
                new DepartmentCode(departmentCode),
                departmentType,
                status);
    }
}
