package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.secondary.DepartmentAssignmentRunServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorJobServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.service.OperatorJobRegistry;
import com.warehouse.platformadministration.domain.enumeration.OperatorJobType;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.vo.OperatorJobRun;
import com.warehouse.platformadministration.domain.vo.OperatorProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OperatorAdminActionPortImplTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    private final OperatorServicePort operatorServicePort = mock(OperatorServicePort.class);

    private final OperatorJobServicePort planningJob = mock(OperatorJobServicePort.class);

    private final DepartmentAssignmentRunServicePort taskServicePort = mock(DepartmentAssignmentRunServicePort.class);

    private OperatorAdminActionPortImpl operatorAdminActionPort;

    @BeforeEach
    void setUp() {
        when(planningJob.jobType()).thenReturn(OperatorJobType.COURIER_ASSIGNMENT_ORCHESTRATION);
        operatorAdminActionPort = new OperatorAdminActionPortImpl(operatorServicePort,
                new OperatorJobRegistry(List.of(planningJob)), taskServicePort);
    }

    @Test
    void shouldListRegisteredJobs() {
        assertThat(operatorAdminActionPort.jobTypes()).containsExactly(OperatorJobType.COURIER_ASSIGNMENT_ORCHESTRATION);
    }

    @Test
    void shouldRunSelectedJobForActiveOperator() {
        final OperatorJobRun run = new OperatorJobRun(OperatorJobType.COURIER_ASSIGNMENT_ORCHESTRATION, false, 3, 2);
        when(operatorServicePort.findProfile(OPERATOR_ID)).thenReturn(Optional.of(profile("ACTIVE")));
        when(planningJob.run(OPERATOR_ID)).thenReturn(run);

        assertThat(operatorAdminActionPort.runJob(OPERATOR_ID, OperatorJobType.COURIER_ASSIGNMENT_ORCHESTRATION))
                .isEqualTo(run);
    }

    @Test
    void shouldRejectJobThatIsNotRegistered() {
        when(operatorServicePort.findProfile(OPERATOR_ID)).thenReturn(Optional.of(profile("ACTIVE")));

        assertThatThrownBy(() -> operatorAdminActionPort.runJob(OPERATOR_ID,
                OperatorJobType.DRIVER_LICENSE_VALIDATION)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectJobForInactiveOperator() {
        when(operatorServicePort.findProfile(OPERATOR_ID)).thenReturn(Optional.of(profile("INACTIVE")));

        assertThatThrownBy(() -> operatorAdminActionPort.runJob(OPERATOR_ID,
                OperatorJobType.COURIER_ASSIGNMENT_ORCHESTRATION)).isInstanceOf(IllegalStateException.class);
        verify(planningJob, never()).run(OPERATOR_ID);
    }

    @Test
    void shouldRejectJobForUnknownOperator() {
        when(operatorServicePort.findProfile(OPERATOR_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> operatorAdminActionPort.runJob(OPERATOR_ID,
                OperatorJobType.COURIER_ASSIGNMENT_ORCHESTRATION)).isInstanceOf(OperatorNotFoundException.class);
    }

    @Test
    void shouldReadLatestCourierAssignmentRuns() {
        when(operatorServicePort.findProfile(OPERATOR_ID)).thenReturn(Optional.of(profile("INACTIVE")));
        when(taskServicePort.findLatest(OPERATOR_ID, OperatorAdminActionPortImpl.COURIER_ASSIGNMENT_RUNS_LIMIT))
                .thenReturn(List.of());

        assertThat(operatorAdminActionPort.findCourierAssignmentRuns(OPERATOR_ID)).isEmpty();
    }

    private static OperatorProfile profile(final String status) {
        return new OperatorProfile(OPERATOR_ID, "Acme", "1234567890", "office@acme.pl", "+48123", null, null, null,
                status, Instant.parse("2026-11-01T10:00:00Z"));
    }
}
