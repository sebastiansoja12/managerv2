package com.warehouse.process.infrastructure.adapter.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.process.ProcessHubTestFixtures;
import com.warehouse.process.domain.port.primary.ProcessPort;
import com.warehouse.process.infrastructure.dto.ProcessDetailsDto;
import com.warehouse.process.infrastructure.dto.ProcessIdDto;
import com.warehouse.process.infrastructure.dto.ProcessStatusDto;

class ProcessResourceAdapterTest {

    private final ProcessPort processPort = mock(ProcessPort.class);

    private final ProcessResourceAdapter adapter = new ProcessResourceAdapter(processPort,
            mock(CurrentOperatorService.class));

    @Test
    void shouldReturnProcessDetailsWithoutRequestPayloads() {
        ProcessHubTestFixtures.initializeDomainContext();
        when(processPort.findByIdForCurrentDepartment(ProcessHubTestFixtures.processId()))
                .thenReturn(ProcessHubTestFixtures.finishedProcessLog());

        final ProcessDetailsDto process = adapter.getProcess(new ProcessIdDto(ProcessHubTestFixtures.processId().value()));

        assertThat(process.processId().value()).isEqualTo(ProcessHubTestFixtures.processId().value());
        assertThat(process.status()).isEqualTo(ProcessStatusDto.SUCCESS);
        assertThat(process.deviceId().value()).isEqualTo("TL:test-device-01");
        assertThat(process.userId().value()).isEqualTo(100L);
        assertThat(process.steps()).isNotEmpty();
        assertThat(process.steps()).allSatisfy(step -> assertThat(step.processType()).isEqualTo(ProcessType.REJECT));
        assertThat(process.toString()).doesNotContain("reject-request", "update-response", "initial-request");
    }

    @Test
    void shouldReturnProcessWithoutStepsWhenNothingWasCommunicated() {
        when(processPort.findByIdForCurrentDepartment(ProcessHubTestFixtures.processId()))
                .thenReturn(ProcessHubTestFixtures.processLog());

        final ProcessDetailsDto process = adapter.getProcess(new ProcessIdDto(ProcessHubTestFixtures.processId().value()));

        assertThat(process.createdAt()).isEqualTo(ProcessHubTestFixtures.CREATED_AT);
        assertThat(process.steps()).isEmpty();
    }
}
