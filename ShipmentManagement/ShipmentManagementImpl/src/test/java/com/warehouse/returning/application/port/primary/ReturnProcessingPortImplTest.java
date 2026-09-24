package com.warehouse.returning.application.port.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.application.port.primary.ReturnProcessingPortImpl;
import com.warehouse.returning.application.port.primary.command.ApplyReturnProcessingStartedCommand;
import com.warehouse.returning.application.port.secondary.ReturnConsumedEventServicePort;
import com.warehouse.returning.application.port.secondary.ShipmentServicePort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;
import com.warehouse.returning.application.port.primary.command.ApplyReturnCancelledCommand;
import com.warehouse.returning.domain.model.ReturnableShipment;
import com.warehouse.commonassets.enumeration.ShipmentStatus;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReturnProcessingPortImplTest {
    private final ShipmentServicePort shipments = mock(ShipmentServicePort.class);
    private final ReturnConsumedEventServicePort consumedEvents = mock(ReturnConsumedEventServicePort.class);
    private final PlatformTransactionManager transactions = mock(PlatformTransactionManager.class);
    private final TransactionStatus transaction = mock(TransactionStatus.class);
    private final OperatorContext context = new OperatorContext();
    private final ReturnProcessingPortImpl port = new ReturnProcessingPortImpl(
            shipments, consumedEvents, context, new TransactionTemplate(transactions));
    private final ApplyReturnProcessingStartedCommand command = new ApplyReturnProcessingStartedCommand(
            UUID.randomUUID(), new ShipmentId(456L), new ReturnPackageId(10L),
            new DepartmentId(3L), new OperatorId(7L), new UserId(11L));

    @AfterEach
    void clearContext() {
        context.clear();
    }

    @Test
    void shouldChangeShipmentAndPublishWithEventContextUntilTransactionCommits() {
        context.assignOperatorContext(new OperatorId(99L), new UserId(98L), new DepartmentId(97L));
        final Authentication previous = SecurityContextHolder.getContext().getAuthentication();
        when(transactions.getTransaction(any())).thenAnswer(invocation -> {
            assertEventContext();
            return transaction;
        });
        when(consumedEvents.tryConsume(command.eventId())).thenReturn(true);
        doAnswer(invocation -> { assertEventContext(); return null; })
                .when(shipments).markReturned(command.shipmentId());
        doAnswer(invocation -> { assertEventContext(); return null; }).when(transactions).commit(transaction);

        port.applyProcessingStarted(command);

        verify(shipments).markReturned(command.shipmentId());
        verify(transactions).commit(transaction);
        assertSame(previous, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldIgnoreRedeliveryBeforeLoadingOrChangingShipment() {
        when(transactions.getTransaction(any())).thenReturn(transaction);
        when(consumedEvents.tryConsume(command.eventId())).thenReturn(false);

        port.applyProcessingStarted(command);

        verifyNoInteractions(shipments);
        verify(transactions).commit(transaction);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldRollBackAndRestoreContextWhenPublicationFails() {
        when(transactions.getTransaction(any())).thenReturn(transaction);
        when(consumedEvents.tryConsume(command.eventId())).thenReturn(true);
        doThrow(new IllegalStateException("Outbox unavailable")).when(shipments).markReturned(command.shipmentId());

        assertThrows(IllegalStateException.class, () -> port.applyProcessingStarted(command));

        verify(transactions).rollback(transaction);
        verify(transactions, never()).commit(any());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private void assertEventContext() {
        final UsernameTenantPasswordAuthenticationToken authentication =
                (UsernameTenantPasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertEquals(command.operatorId(), authentication.getOperatorId());
        assertEquals(command.departmentId(), authentication.getDepartmentId());
        assertEquals(command.userId(), authentication.getPrincipal());
    }

    @Test
    void shouldCancelLinkedShipmentAndRestoreOriginalWithEventContext() {
        final ShipmentId linkedShipmentId = new ShipmentId(789L);
        when(transactions.getTransaction(any())).thenReturn(transaction);
        when(consumedEvents.tryConsume(command.eventId())).thenReturn(true);
        when(shipments.getShipment(command.shipmentId())).thenAnswer(invocation -> {
            assertEventContext();
            return new ReturnableShipment(command.shipmentId(), ShipmentStatus.RETURN, linkedShipmentId);
        });

        port.applyCancelled(cancellation());

        final org.mockito.InOrder order = inOrder(shipments, consumedEvents, transactions);
        order.verify(shipments).cancelReturnShipment(linkedShipmentId);
        order.verify(shipments).restoreAfterReturnCancellation(command.shipmentId());
        order.verify(consumedEvents).markCancelled(command.returnPackageId());
        order.verify(transactions).commit(transaction);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldRestoreOriginalWithoutLinkedReturnShipment() {
        when(transactions.getTransaction(any())).thenReturn(transaction);
        when(consumedEvents.tryConsume(command.eventId())).thenReturn(true);
        when(shipments.getShipment(command.shipmentId()))
                .thenReturn(new ReturnableShipment(command.shipmentId(), ShipmentStatus.RETURN, null));

        port.applyCancelled(cancellation());

        verify(shipments).restoreAfterReturnCancellation(command.shipmentId());
        verify(shipments, never()).cancelReturnShipment(any());
        verify(consumedEvents).markCancelled(command.returnPackageId());
    }

    @Test
    void shouldIgnoreDuplicateCancellationAndLateProcessing() {
        when(transactions.getTransaction(any())).thenReturn(transaction);
        when(consumedEvents.lockAndCheckCancelled(command.returnPackageId())).thenReturn(true);

        port.applyCancelled(cancellation());
        port.applyProcessingStarted(command);

        verifyNoInteractions(shipments);
        verify(consumedEvents, never()).tryConsume(any());
    }

    @Test
    void shouldRollBackCancellationWhenLinkedShipmentCannotBeCancelled() {
        final ShipmentId linkedShipmentId = new ShipmentId(789L);
        when(transactions.getTransaction(any())).thenReturn(transaction);
        when(consumedEvents.tryConsume(command.eventId())).thenReturn(true);
        when(shipments.getShipment(command.shipmentId()))
                .thenReturn(new ReturnableShipment(command.shipmentId(), ShipmentStatus.RETURN, linkedShipmentId));
        doThrow(new IllegalStateException("Persistence failed")).when(shipments).cancelReturnShipment(linkedShipmentId);

        assertThrows(IllegalStateException.class, () -> port.applyCancelled(cancellation()));

        verify(transactions).rollback(transaction);
        verify(consumedEvents, never()).markCancelled(any());
        verify(shipments, never()).restoreAfterReturnCancellation(any());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private ApplyReturnCancelledCommand cancellation() {
        return new ApplyReturnCancelledCommand(command.eventId(), command.shipmentId(), command.returnPackageId(),
                command.departmentId(), command.operatorId(), command.userId());
    }
}
