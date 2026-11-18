package com.warehouse.returning.application.port.primary;

import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.returning.application.port.primary.command.CreateReturnPackageCommand;
import com.warehouse.returning.application.port.secondary.CurrentUserServicePort;
import com.warehouse.returning.application.port.secondary.DepartmentServicePort;
import com.warehouse.returning.application.port.secondary.ReturningTrackManagerServicePort;
import com.warehouse.returning.application.port.secondary.ShipmentServicePort;
import com.warehouse.returning.application.service.ReturnDepartmentAccessService;
import com.warehouse.returning.domain.exception.ReturnDepartmentAccessDeniedException;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnableShipment;
import com.warehouse.returning.domain.vo.CreateReturnRequest;
import com.warehouse.returning.domain.vo.CreatedReturn;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnState;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReturnPortImplTest {
    private final ReturningTrackManagerServicePort returns = mock(ReturningTrackManagerServicePort.class);
    private final ShipmentServicePort shipments = mock(ShipmentServicePort.class);
    private final DepartmentServicePort departments = mock(DepartmentServicePort.class);
    private final PlatformTransactionManager transactions = mock(PlatformTransactionManager.class);
    private final TransactionStatus transaction = mock(TransactionStatus.class);
    private final CurrentUserServicePort currentUser = mock(CurrentUserServicePort.class);
    private final ReturnPortImpl port = new ReturnPortImpl(returns, shipments, departments,
            new TransactionTemplate(transactions), new ReturnDepartmentAccessService(currentUser));
    private final ShipmentId shipmentId = new ShipmentId(456L);
    private final ShipmentId draftShipmentId = new ShipmentId(789L);
    private final TrackingNumber trackingNumber = new TrackingNumber("TRK456");
    private final CreateReturnPackageCommand command = new CreateReturnPackageCommand(trackingNumber, "Damaged parcel",
            new DepartmentId(3L), new UserId(11L), ReasonCode.DAMAGED);

    @Test
    void shouldRegisterReturnWithDraftReturnShipment() {
        givenDeliveredShipment();
        when(returns.create(any())).thenReturn(List.of(
                new CreatedReturn(shipmentId, draftShipmentId, new ReturnPackageId(10L), ReturnStatus.CREATED)));

        final List<CreatedReturn> createdReturns = port.create(command);

        final ArgumentCaptor<CreateReturnRequest> request = ArgumentCaptor.forClass(CreateReturnRequest.class);
        verify(returns).create(request.capture());
        assertEquals(draftShipmentId, request.getValue().returnShipmentId());
        assertEquals(draftShipmentId, createdReturns.getFirst().returnShipmentId());
        verify(transactions).commit(transaction);
    }

    @Test
    void shouldRollBackDraftWhenReturnAlreadyExists() {
        givenDeliveredShipment();
        when(returns.create(any())).thenReturn(List.of(
                new CreatedReturn(shipmentId, new ShipmentId(111L), new ReturnPackageId(10L), ReturnStatus.CREATED)));

        assertThrows(IllegalArgumentException.class, () -> port.create(command));

        verify(transactions).rollback(transaction);
        verify(transactions, never()).commit(any());
    }

    @Test
    void shouldRollBackDraftWhenReturnCannotBeRegistered() {
        givenDeliveredShipment();
        when(returns.create(any())).thenThrow(new IllegalStateException("RTM unavailable"));

        assertThrows(IllegalStateException.class, () -> port.create(command));

        verify(transactions).rollback(transaction);
    }

    @Test
    void shouldNotCreateDraftForShipmentThatCannotBeReturned() {
        when(shipments.getShipment(trackingNumber))
                .thenReturn(new ReturnableShipment(shipmentId, trackingNumber, ShipmentStatus.SENT, null));

        assertThrows(IllegalArgumentException.class, () -> port.create(command));

        verify(shipments, never()).createReturnShipmentDraft(any());
        verifyNoInteractions(returns);
    }

    @Test
    void shouldNotRegisterReturnForOtherDepartment() {
        givenDeliveredShipment();
        when(currentUser.canAccessDepartment(command.getDepartmentId())).thenReturn(false);

        assertThrows(ReturnDepartmentAccessDeniedException.class, () -> port.create(command));

        verify(shipments, never()).createReturnShipmentDraft(any());
    }

    @Test
    void shouldNotCompleteReturnOfOtherDepartment() {
        final ReturnPackageId returnId = new ReturnPackageId(10L);
        when(returns.get(returnId)).thenReturn(new ReturnState(returnId, shipmentId, ReturnStatus.PROCESSING, null,
                draftShipmentId, null, 1L, new DepartmentId(5L), null));

        assertThrows(ReturnDepartmentAccessDeniedException.class, () -> port.complete(returnId));

        verify(returns, never()).complete(any());
    }

    @Test
    void shouldCompleteReturnOfOwnDepartment() {
        final ReturnPackageId returnId = new ReturnPackageId(10L);
        final DepartmentId departmentId = new DepartmentId(3L);
        when(returns.get(returnId)).thenReturn(new ReturnState(returnId, shipmentId, ReturnStatus.PROCESSING, null,
                draftShipmentId, null, 1L, departmentId, null));
        when(currentUser.canAccessDepartment(departmentId)).thenReturn(true);

        port.complete(returnId);

        verify(returns).complete(returnId);
    }

    private void givenDeliveredShipment() {
        when(shipments.getShipment(trackingNumber))
                .thenReturn(new ReturnableShipment(shipmentId, trackingNumber, ShipmentStatus.DELIVERY, null));
        when(departments.exists(command.getDepartmentId())).thenReturn(true);
        when(currentUser.canAccessDepartment(command.getDepartmentId())).thenReturn(true);
        when(transactions.getTransaction(any())).thenReturn(transaction);
        when(shipments.createReturnShipmentDraft(shipmentId)).thenReturn(draftShipmentId);
    }
}
