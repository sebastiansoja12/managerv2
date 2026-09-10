package com.warehouse.deliverynetwork.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.deliverynetwork.application.exception.MissingOperatorContextException;
import com.warehouse.deliverynetwork.application.port.primary.command.DepartmentConnectionCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.DepartmentConnectionCodeCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.DepartmentImportCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkByCodesCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkCommand;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkExportResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkResult;
import com.warehouse.deliverynetwork.application.port.secondary.DeliveryNetworkRepository;
import com.warehouse.deliverynetwork.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;
import com.warehouse.deliverynetwork.domain.model.DeliveryNetwork;
import com.warehouse.deliverynetwork.domain.event.DeliveryNetworkChanged;
import com.warehouse.deliverynetwork.domain.service.DeliveryPathFinder;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryNetworkPortImplTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(10L);

    @Mock
    private DeliveryNetworkRepository deliveryNetworkRepository;

    @Mock
    private DepartmentDirectoryServicePort departmentDirectoryServicePort;

    @Mock
    private OperatorContextProvider operatorContextProvider;

    @Mock
    private DomainEventPublisher domainEventPublisher;

    private DeliveryNetworkPort deliveryNetworkPort;

    @BeforeEach
    void setUp() {
        this.deliveryNetworkPort = new DeliveryNetworkPortImpl(
                this.deliveryNetworkRepository,
                this.departmentDirectoryServicePort,
                this.operatorContextProvider,
                new DeliveryPathFinder(),
                this.domainEventPublisher);
    }

    @Test
    void shouldReplaceAndPersistCurrentOperatorNetwork() {
        when(this.operatorContextProvider.currentOperatorId()).thenReturn(Optional.of(OPERATOR_ID));
        when(this.deliveryNetworkRepository.find()).thenReturn(Optional.empty());
        when(this.departmentDirectoryServicePort.getCurrentOperatorDepartments()).thenReturn(List.of(
                branch(1L, "KT1"),
                sortingFacility(2L, "NCS")));
        final ReplaceDeliveryNetworkCommand command = new ReplaceDeliveryNetworkCommand(List.of(
                new DepartmentConnectionCommand(departmentId(2L), departmentId(1L))));

        final DeliveryNetworkResult result = this.deliveryNetworkPort.replaceCurrentNetwork(command);

        final DepartmentConnection expectedConnection = connection(1L, 2L);
        final ArgumentCaptor<DeliveryNetwork> networkCaptor = ArgumentCaptor.forClass(DeliveryNetwork.class);
        final ArgumentCaptor<DeliveryNetworkChanged> eventCaptor = ArgumentCaptor.forClass(DeliveryNetworkChanged.class);
        verify(this.deliveryNetworkRepository).save(networkCaptor.capture());
        verify(this.domainEventPublisher).publish(eventCaptor.capture());
        assertEquals(OPERATOR_ID, networkCaptor.getValue().operatorId());
        assertEquals(Set.of(expectedConnection), networkCaptor.getValue().connections());
        assertEquals(Set.of(expectedConnection), result.connections());
        assertEquals(OPERATOR_ID, eventCaptor.getValue().getSnapshot().operatorId());
        assertEquals(Set.of(expectedConnection), eventCaptor.getValue().getSnapshot().connections());
    }

    @Test
    void shouldReturnEmptyNetworkWhenOperatorHasNoConfiguration() {
        when(this.operatorContextProvider.currentOperatorId()).thenReturn(Optional.of(OPERATOR_ID));
        when(this.deliveryNetworkRepository.find()).thenReturn(Optional.empty());

        final DeliveryNetworkResult result = this.deliveryNetworkPort.getCurrentNetwork();

        assertEquals(Set.of(), result.connections());
    }

    @Test
    void shouldReplaceNetworkUsingCaseInsensitiveDepartmentCodes() {
        when(this.operatorContextProvider.currentOperatorId()).thenReturn(Optional.of(OPERATOR_ID));
        when(this.deliveryNetworkRepository.find()).thenReturn(Optional.empty());
        when(this.departmentDirectoryServicePort.getCurrentOperatorDepartments()).thenReturn(List.of(
                branch(1L, "KT1"),
                sortingFacility(2L, "NCS")));
        final ReplaceDeliveryNetworkByCodesCommand command = new ReplaceDeliveryNetworkByCodesCommand(
                List.of(
                        new DepartmentImportCommand(
                                new DepartmentCode("KT1"), DepartmentType.BRANCH, DepartmentStatus.ACTIVE),
                        new DepartmentImportCommand(
                                new DepartmentCode("NCS"),
                                DepartmentType.SORTING_FACILITY,
                                DepartmentStatus.ACTIVE)),
                List.of(
                        new DepartmentConnectionCodeCommand(
                                new DepartmentCode("kt1"),
                                new DepartmentCode("ncs"))));

        final DeliveryNetworkResult result =
                this.deliveryNetworkPort.replaceCurrentNetworkByDepartmentCodes(command);

        assertEquals(Set.of(connection(1L, 2L)), result.connections());
        verify(this.deliveryNetworkRepository).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldExportNetworkUsingDepartmentCodes() {
        when(this.operatorContextProvider.currentOperatorId()).thenReturn(Optional.of(OPERATOR_ID));
        when(this.deliveryNetworkRepository.find()).thenReturn(Optional.of(new DeliveryNetwork(
                OPERATOR_ID,
                Set.of(connection(1L, 2L)))));
        when(this.departmentDirectoryServicePort.getCurrentOperatorDepartments()).thenReturn(List.of(
                branch(1L, "KT1"),
                sortingFacility(2L, "NCS")));

        final DeliveryNetworkExportResult result = this.deliveryNetworkPort.getCurrentNetworkForExport();

        assertEquals(List.of("KT1", "NCS"), result.departments().stream()
                .map(department -> department.departmentCode().getValue())
                .toList());
        assertEquals("KT1", result.connections().getFirst().firstDepartmentCode().getValue());
        assertEquals("NCS", result.connections().getFirst().secondDepartmentCode().getValue());
    }

    @Test
    void shouldFindIndirectDeliveryPath() {
        final DeliveryNetwork deliveryNetwork = new DeliveryNetwork(OPERATOR_ID, Set.of(
                connection(1L, 2L),
                connection(2L, 3L)));
        when(this.operatorContextProvider.currentOperatorId()).thenReturn(Optional.of(OPERATOR_ID));
        when(this.deliveryNetworkRepository.find())
                .thenReturn(Optional.of(deliveryNetwork));

        final DeliveryPath deliveryPath = this.deliveryNetworkPort.findDeliveryPath(
                departmentId(1L), departmentId(3L));

        assertEquals(List.of(departmentId(1L), departmentId(2L), departmentId(3L)), deliveryPath.departmentIds());
    }

    @Test
    void shouldRemoveAndPersistAllConnectionsForArchivedDepartment() {
        final DeliveryNetwork deliveryNetwork = new DeliveryNetwork(OPERATOR_ID, Set.of(
                connection(1L, 2L),
                connection(2L, 3L),
                connection(3L, 4L)));
        when(this.operatorContextProvider.currentOperatorId()).thenReturn(Optional.of(OPERATOR_ID));
        when(this.deliveryNetworkRepository.find()).thenReturn(Optional.of(deliveryNetwork));

        this.deliveryNetworkPort.removeDepartmentConnections(departmentId(2L));

        final ArgumentCaptor<DeliveryNetwork> networkCaptor = ArgumentCaptor.forClass(DeliveryNetwork.class);
        verify(this.deliveryNetworkRepository).save(networkCaptor.capture());
        verify(this.domainEventPublisher).publish(org.mockito.ArgumentMatchers.any(DeliveryNetworkChanged.class));
        assertEquals(Set.of(connection(3L, 4L)), networkCaptor.getValue().connections());
    }

    @Test
    void shouldRequireCurrentOperatorContext() {
        when(this.operatorContextProvider.currentOperatorId()).thenReturn(Optional.empty());

        assertThrows(MissingOperatorContextException.class, () -> this.deliveryNetworkPort.getCurrentNetwork());
    }

    private static DepartmentNode branch(final Long departmentId, final String departmentCode) {
        return department(departmentId, departmentCode, DepartmentType.BRANCH);
    }

    private static DepartmentNode sortingFacility(final Long departmentId, final String departmentCode) {
        return department(departmentId, departmentCode, DepartmentType.SORTING_FACILITY);
    }

    private static DepartmentNode department(
            final Long departmentId,
            final String departmentCode,
            final DepartmentType departmentType) {
        return new DepartmentNode(
                new DepartmentId(departmentId),
                new DepartmentCode(departmentCode),
                departmentType,
                DepartmentStatus.ACTIVE);
    }

    private static DepartmentConnection connection(final Long firstDepartmentId, final Long secondDepartmentId) {
        return new DepartmentConnection(departmentId(firstDepartmentId), departmentId(secondDepartmentId));
    }

    private static DepartmentId departmentId(final Long value) {
        return new DepartmentId(value);
    }
}
