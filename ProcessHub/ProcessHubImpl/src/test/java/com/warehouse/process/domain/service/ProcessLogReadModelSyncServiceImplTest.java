package com.warehouse.process.domain.service;

import static com.warehouse.process.ProcessHubTestFixtures.finishedProcessLog;
import static com.warehouse.process.ProcessHubTestFixtures.processId;
import static com.warehouse.process.ProcessHubTestFixtures.processLog;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.port.secondary.ProcessLogReadModelRepository;

@ExtendWith(MockitoExtension.class)
class ProcessLogReadModelSyncServiceImplTest {

    @Mock
    private ProcessLogReadModelRepository repository;

    private ProcessLogReadModelSyncServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProcessLogReadModelSyncServiceImpl(repository);
    }

    @Test
    void shouldApplySnapshotOnExistingDomainReadModelAndSyncIt() {
        final ProcessLog existing = processLog();
        final ProcessLog snapshotSource = finishedProcessLog();
        when(repository.findById(processId())).thenReturn(Optional.of(existing));

        service.syncProcessLog(snapshotSource.snapshot());

        final ArgumentCaptor<ProcessLog> captor = ArgumentCaptor.forClass(ProcessLog.class);
        verify(repository).syncProcessLog(captor.capture());

        assertThat(captor.getValue()).isSameAs(existing);
        assertThat(captor.getValue().getStatus()).isEqualTo(ProcessStatus.SUCCESS);
        assertThat(captor.getValue().getCommunicationLogDetails().getCommunicationLogDetails()).hasSize(2);
    }

    @Test
    void shouldCreateDomainReadModelFromSnapshotWhenItDoesNotExistYet() {
        final ProcessLog snapshotSource = finishedProcessLog();
        when(repository.findById(processId())).thenReturn(Optional.empty());

        service.syncProcessLog(snapshotSource.snapshot());

        final ArgumentCaptor<ProcessLog> captor = ArgumentCaptor.forClass(ProcessLog.class);
        verify(repository).syncProcessLog(captor.capture());

        assertThat(captor.getValue().getProcessId()).isEqualTo(processId());
        assertThat(captor.getValue().getStatus()).isEqualTo(ProcessStatus.SUCCESS);
        assertThat(captor.getValue().getCommunicationLogDetails().getCommunicationLogDetails()).hasSize(2);
    }
}
