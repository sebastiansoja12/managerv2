package com.warehouse.returning.application.service;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.event.ReturnPackageCompleted;
import com.warehouse.returning.domain.event.ReturnPackageProcessingStarted;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;
import com.warehouse.returning.domain.exception.StatusChangeException;
import static org.mockito.Mockito.*;

class ReturnProcessingServiceTest {
    private final ReturnRepository repository = mock(ReturnRepository.class);
    private final ApplicationEventPublisher events = mock(ApplicationEventPublisher.class);
    private final ReturnProcessingService service = new ReturnProcessingService(repository, events);

    @Test
    void shouldPersistProcessingBeforePublishingEvent() {
        final ReturnPackage returning = ReturnProcessingFixture.returning(ReturnStatus.CREATED);

        when(repository.findForProcessing(returning.getReturnPackageId())).thenReturn(returning);

        service.startProcessing(returning.getReturnPackageId());

        final InOrder order = inOrder(repository, events);
        order.verify(repository).createOrUpdate(returning);
        final ArgumentCaptor<ReturnPackageProcessingStarted> event = ArgumentCaptor.forClass(ReturnPackageProcessingStarted.class);
        order.verify(events).publishEvent(event.capture());
        verify(events, times(1)).publishEvent(any(Object.class));
        verify(repository, times(1)).createOrUpdate(any());
        assertEquals(ReturnStatus.PROCESSING, event.getValue().getSnapshot().returnStatus());
        assertEquals(returning.getReturnPackageId(), event.getValue().getSnapshot().returnPackageId());
        assertEquals(returning.getAssignedDepartmentId(), event.getValue().getSnapshot().assignedDepartmentId());
    }

    @ParameterizedTest
    @EnumSource(value = ReturnStatus.class, names = {"PROCESSING", "COMPLETED", "CANCELLED"})
    void shouldRejectProcessingWhenReturnIsNotCreated(final ReturnStatus status) {
        final ReturnPackage returning = ReturnProcessingFixture.returning(status);
        when(repository.findForProcessing(returning.getReturnPackageId())).thenReturn(returning);

        assertThrows(StatusChangeException.class, () -> service.startProcessing(returning.getReturnPackageId()));

        verify(repository, never()).createOrUpdate(any());
        verifyNoInteractions(events);
    }

    @Test
    void shouldNotPublishWhenPersistenceFails() {
        final ReturnPackage returning = ReturnProcessingFixture.returning(ReturnStatus.CREATED);
        when(repository.findForProcessing(returning.getReturnPackageId())).thenReturn(returning);
        doThrow(new IllegalStateException("Database unavailable")).when(repository).createOrUpdate(returning);


        assertThrows(IllegalStateException.class, () -> service.startProcessing(returning.getReturnPackageId()));
        verifyNoInteractions(events);
    }

    @ParameterizedTest
    @EnumSource(value = ReturnStatus.class, names = {"CREATED", "PROCESSING"})
    void shouldPersistCancellationAndPublishOnce(final ReturnStatus initialStatus) {
        final ReturnPackage returning = ReturnProcessingFixture.returning(initialStatus);
        when(repository.findForProcessing(returning.getReturnPackageId())).thenReturn(returning);

        service.cancel(returning.getReturnPackageId());
        service.cancel(returning.getReturnPackageId());

        assertEquals(ReturnStatus.CANCELLED, returning.getReturnStatus());
        final InOrder order = inOrder(repository, events);
        order.verify(repository).createOrUpdate(returning);
        order.verify(events).publishEvent(any(com.warehouse.returning.domain.event.ReturnPackageCanceled.class));
        verify(events, times(1)).publishEvent(any(Object.class));
        verify(repository, times(1)).createOrUpdate(any());
    }

    @Test
    void shouldRejectCancellationOfCompletedReturn() {
        final ReturnPackage returning = ReturnProcessingFixture.returning(ReturnStatus.COMPLETED);
        when(repository.findForProcessing(returning.getReturnPackageId())).thenReturn(returning);

        assertThrows(StatusChangeException.class, () -> service.cancel(returning.getReturnPackageId()));

        verifyNoInteractions(events);
        verify(repository, never()).createOrUpdate(any());
    }

    @Test
    void shouldNotPublishCancellationWhenPersistenceFails() {
        final ReturnPackage returning = ReturnProcessingFixture.returning(ReturnStatus.PROCESSING);
        when(repository.findForProcessing(returning.getReturnPackageId())).thenReturn(returning);
        doThrow(new IllegalStateException("Database unavailable")).when(repository).createOrUpdate(returning);

        assertThrows(IllegalStateException.class, () -> service.cancel(returning.getReturnPackageId()));

        verifyNoInteractions(events);
    }

    @Test
    void shouldPersistCompletionBeforePublishingEvent() {
        final ReturnPackage returning = ReturnProcessingFixture.returning(ReturnStatus.PROCESSING);
        when(repository.findForProcessing(returning.getReturnPackageId())).thenReturn(returning);

        service.complete(returning.getReturnPackageId());

        final InOrder order = inOrder(repository, events);
        order.verify(repository).createOrUpdate(returning);
        final ArgumentCaptor<ReturnPackageCompleted> event = ArgumentCaptor.forClass(ReturnPackageCompleted.class);
        order.verify(events).publishEvent(event.capture());
        assertEquals(ReturnStatus.COMPLETED, event.getValue().getSnapshot().returnStatus());
        assertEquals(returning.getShipmentId(), event.getValue().getSnapshot().shipmentId());
    }

    @ParameterizedTest
    @EnumSource(value = ReturnStatus.class, names = {"CREATED", "COMPLETED", "CANCELLED"})
    void shouldRejectCompletionWhenReturnIsNotProcessing(final ReturnStatus status) {
        final ReturnPackage returning = ReturnProcessingFixture.returning(status);
        when(repository.findForProcessing(returning.getReturnPackageId())).thenReturn(returning);

        assertThrows(StatusChangeException.class, () -> service.complete(returning.getReturnPackageId()));

        verify(repository, never()).createOrUpdate(any());
        verifyNoInteractions(events);
    }

    @Test
    void shouldPersistRegisteredReturnBeforePublishingCreatedEvent() {
        final ReturnPackage returning = ReturnProcessingFixture.returning(ReturnStatus.CREATED);

        service.register(returning);

        final InOrder order = inOrder(repository, events);
        order.verify(repository).createOrUpdate(returning);
        order.verify(events).publishEvent(any(com.warehouse.returning.domain.event.ReturnPackageCreated.class));
    }

    @Test
    void shouldPublishChangeWhenReasonCodeChanges() {
        final ReturnPackage returning = ReturnProcessingFixture.returning(ReturnStatus.CREATED);
        when(repository.findById(returning.getReturnPackageId())).thenReturn(returning);

        service.changeReasonCode(returning.getReturnPackageId(), ReasonCode.WRONG_ITEM);

        assertEquals(ReasonCode.WRONG_ITEM, returning.getReasonCode());
        final InOrder order = inOrder(repository, events);
        order.verify(repository).createOrUpdate(returning);
        order.verify(events).publishEvent(any(com.warehouse.returning.domain.event.ReturnPackageChanged.class));
    }
}
