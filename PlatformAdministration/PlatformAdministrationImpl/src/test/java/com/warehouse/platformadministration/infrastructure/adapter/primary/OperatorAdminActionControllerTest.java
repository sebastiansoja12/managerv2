package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.OperatorAdminActionPort;
import com.warehouse.platformadministration.domain.enumeration.OperatorJobType;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.vo.DepartmentAssignmentRunView;
import com.warehouse.platformadministration.domain.vo.OperatorJobRun;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class OperatorAdminActionControllerTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    private final OperatorAdminActionPort operatorAdminActionPort = mock(OperatorAdminActionPort.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new OperatorAdminActionController(operatorAdminActionPort))
                .setMessageConverters(new JacksonJsonHttpMessageConverter())
                .build();
    }

    @Test
    void shouldListAvailableJobs() throws Exception {
        when(operatorAdminActionPort.jobTypes()).thenReturn(List.of(OperatorJobType.COURIER_ASSIGNMENT_ORCHESTRATION,
                OperatorJobType.COURIER_ASSIGNMENT_RECOVERY));

        mockMvc.perform(get("/platform/operator-jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("COURIER_ASSIGNMENT_ORCHESTRATION"))
                .andExpect(jsonPath("$[1]").value("COURIER_ASSIGNMENT_RECOVERY"));
    }

    @Test
    void shouldRunSelectedJob() throws Exception {
        when(operatorAdminActionPort.runJob(OPERATOR_ID, OperatorJobType.COURIER_ASSIGNMENT_RECOVERY))
                .thenReturn(new OperatorJobRun(OperatorJobType.COURIER_ASSIGNMENT_RECOVERY, true, 4, null));

        mockMvc.perform(post("/operators/20/admin-actions/jobs/COURIER_ASSIGNMENT_RECOVERY"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.jobType").value("COURIER_ASSIGNMENT_RECOVERY"))
                .andExpect(jsonPath("$.asynchronous").value(true))
                .andExpect(jsonPath("$.affected").value(4));
    }

    @Test
    void shouldRejectUnknownJobType() throws Exception {
        mockMvc.perform(post("/operators/20/admin-actions/jobs/NOT_A_JOB"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(operatorAdminActionPort);
    }

    @Test
    void shouldReturnConflictForInactiveOperator() throws Exception {
        when(operatorAdminActionPort.runJob(OPERATOR_ID, OperatorJobType.COURIER_ASSIGNMENT_ORCHESTRATION))
                .thenThrow(new IllegalStateException("inactive"));

        mockMvc.perform(post("/operators/20/admin-actions/jobs/COURIER_ASSIGNMENT_ORCHESTRATION"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNotFoundForUnknownOperator() throws Exception {
        when(operatorAdminActionPort.runJob(OPERATOR_ID, OperatorJobType.COURIER_ASSIGNMENT_ORCHESTRATION))
                .thenThrow(new OperatorNotFoundException(OPERATOR_ID));

        mockMvc.perform(post("/operators/20/admin-actions/jobs/COURIER_ASSIGNMENT_ORCHESTRATION"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnLatestCourierAssignmentRuns() throws Exception {
        final UUID runId = UUID.fromString("4b1d7c52-2f0e-4c8a-9d4e-1c7f0a3b9e11");
        when(operatorAdminActionPort.findCourierAssignmentRuns(OPERATOR_ID)).thenReturn(List.of(
                new DepartmentAssignmentRunView(new DepartmentAssignmentRunId(runId), new DepartmentId(1L),
                        Instant.parse("2026-11-24T07:00:00Z"), Instant.parse("2026-11-24T11:00:00Z"),
                        Instant.parse("2026-11-21T13:00:00Z"), "COMPLETED", 0,
                        Instant.parse("2026-11-24T10:55:00Z"), 10, 9, 8, 1, 1, 0, 5, null,
                        Instant.parse("2026-11-24T11:00:00Z"), Instant.parse("2026-11-24T11:02:00Z"))));

        mockMvc.perform(get("/operators/20/admin-actions/courier-assignment-runs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].runId").value(runId.toString()))
                .andExpect(jsonPath("$[0].departmentId").value(1))
                .andExpect(jsonPath("$[0].status").value("COMPLETED"))
                .andExpect(jsonPath("$[0].assigned").value(8));
    }
}
