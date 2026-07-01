package com.warehouse.process.infrastructure.adapter.secondary;

import static com.warehouse.process.ProcessHubTestFixtures.finishedProcessLog;
import static com.warehouse.process.ProcessHubTestFixtures.processId;
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

import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.ProcessLogReadEntity;
import com.warehouse.process.infrastructure.adapter.secondary.mapper.ProcessLogToReadEntityMapper;

@ExtendWith(MockitoExtension.class)
class ProcessLogReadModelRepositoryImplTest {

    @Mock
    private ProcessLogReadRepository processLogReadRepository;

    private ProcessLogReadModelRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new ProcessLogReadModelRepositoryImpl(processLogReadRepository);
    }

    @Test
    void shouldSaveMappedReadModelWithAllCommunicationLogs() {
        repository.syncProcessLog(finishedProcessLog());

        final ArgumentCaptor<ProcessLogReadEntity> captor = ArgumentCaptor.forClass(ProcessLogReadEntity.class);
        verify(processLogReadRepository).save(captor.capture());

        assertThat(captor.getValue().getProcessId()).isEqualTo(processId());
        assertThat(captor.getValue().getCommunicationLogs()).hasSize(2);
    }

    @Test
    void shouldMapReadEntityToDomainModelWhenFindingById() {
        final ProcessLogReadEntity entity = ProcessLogToReadEntityMapper.map(finishedProcessLog());
        when(processLogReadRepository.findById(processId())).thenReturn(Optional.of(entity));

        final Optional<ProcessLog> found = repository.findById(processId());

        assertThat(found).isPresent();
        assertThat(found.orElseThrow().getCommunicationLogDetails().getCommunicationLogDetails()).hasSize(2);
    }
}
