package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.logisticsdelivery.application.port.primary.result.AssignmentOrchestration;
import com.warehouse.logisticsdelivery.application.port.secondary.DepartmentAssignmentConfigServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.DepartmentAssignmentRunRepository;
import com.warehouse.logisticsdelivery.domain.enumeration.AssignmentWindowType;
import com.warehouse.logisticsdelivery.domain.enumeration.DepartmentAssignmentRunStatus;
import com.warehouse.logisticsdelivery.domain.event.DepartmentAssignmentRunCreated;
import com.warehouse.logisticsdelivery.domain.model.DepartmentAssignmentRun;
import com.warehouse.logisticsdelivery.domain.service.AssignmentWindowCalculator;
import com.warehouse.logisticsdelivery.domain.service.DepartmentAssignmentRunFactory;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.transaction.support.TransactionOperations;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.WARSAW;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.config;
import static com.warehouse.logisticsdelivery.DeliveryFixture.DESTINATION_DEPARTMENT_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AssignmentOrchestratorTest {

    private static final Instant NOW = ZonedDateTime.of(2026, 11, 24, 14, 0, 0, 0, WARSAW).toInstant();
    private static final DepartmentAssignmentConfig MORNING = config(AssignmentWindowType.TIME_OF_DAY,
            LocalTime.of(8, 0), LocalTime.of(12, 0));

    private DepartmentAssignmentConfigServicePort configServicePort;
    private DepartmentAssignmentRunRepository runRepository;
    private DomainEventPublisher domainEventPublisher;
    private AssignmentOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        configServicePort = mock(DepartmentAssignmentConfigServicePort.class);
        runRepository = mock(DepartmentAssignmentRunRepository.class);
        domainEventPublisher = mock(DomainEventPublisher.class);
        final Clock clock = Clock.fixed(NOW, WARSAW);
        orchestrator = new AssignmentOrchestrator(configServicePort, runRepository,
                new DepartmentAssignmentRunFactory(clock), new AssignmentWindowCalculator(), domainEventPublisher,
                TransactionOperations.withoutTransaction(), clock);
    }

    @Test
    void shouldCreatePendingRunAndPublishEvent() {
        when(configServicePort.findConfigs()).thenReturn(List.of(MORNING));
        when(runRepository.findLastWatermark(DESTINATION_DEPARTMENT_ID)).thenReturn(Optional.empty());
        final ArgumentCaptor<DepartmentAssignmentRun> created = ArgumentCaptor.forClass(DepartmentAssignmentRun.class);

        final AssignmentOrchestration orchestration = orchestrator.orchestrate();

        verify(runRepository).create(created.capture());
        assertThat(orchestration.triggeredDepartments(), is(1));
        assertThat(created.getValue().getStatus(), is(DepartmentAssignmentRunStatus.PENDING));
        assertThat(created.getValue().getWindow().from(), equalTo(Instant.parse("2026-11-24T07:00:00Z")));
        verify(domainEventPublisher).publish(any(DepartmentAssignmentRunCreated.class));
    }

    @Test
    void shouldSkipDepartmentWithActiveRun() {
        when(configServicePort.findConfigs()).thenReturn(List.of(MORNING));
        when(runRepository.existsActive(DESTINATION_DEPARTMENT_ID)).thenReturn(true);

        final AssignmentOrchestration orchestration = orchestrator.orchestrate();

        assertThat(orchestration.departmentsWithActiveRun(), is(1));
        verify(runRepository, never()).create(any());
        verify(domainEventPublisher, never()).publish(any());
    }

    @Test
    void shouldSkipDisabledDepartment() {
        final DepartmentAssignmentConfig disabled = new DepartmentAssignmentConfig(MORNING.departmentId(), false,
                MORNING.dateField(), MORNING.windowType(), MORNING.windowStart(), MORNING.windowEnd(),
                MORNING.timeZone(), MORNING.processingDelay(), MORNING.overlap(), MORNING.maxWindow(),
                MORNING.lookback(), MORNING.batchSize());
        when(configServicePort.findConfigs()).thenReturn(List.of(disabled));

        final AssignmentOrchestration orchestration = orchestrator.orchestrate();

        assertThat(orchestration.disabledDepartments(), is(1));
        verify(runRepository, never()).existsActive(any());
    }

    @Test
    void shouldNotCreateRunForEmptyWindow() {
        when(configServicePort.findConfigs()).thenReturn(List.of(MORNING));
        when(runRepository.findLastWatermark(DESTINATION_DEPARTMENT_ID))
                .thenReturn(Optional.of(Instant.parse("2026-11-24T11:00:00Z")));

        final AssignmentOrchestration orchestration = orchestrator.orchestrate();

        assertThat(orchestration.departmentsWithEmptyWindow(), is(1));
        verify(runRepository, never()).create(any());
    }
}
