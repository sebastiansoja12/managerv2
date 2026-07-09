package com.warehouse.process.infrastructure.adapter.secondary;

import static com.warehouse.process.ProcessHubTestFixtures.processId;
import static com.warehouse.process.ProcessHubTestFixtures.processLog;
import static com.warehouse.process.ProcessHubTestFixtures.shipmentRejected;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.infrastructure.adapter.secondary.entity.write.ProcessLogWriteEntity;

@ExtendWith(MockitoExtension.class)
class ProcessRepositoryImplTest {

    @Mock
    private ProcessLogReadRepository readRepository;

    @Mock
    private ProcessLogWriteRepository writeRepository;

    private ProcessRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new ProcessRepositoryImpl(readRepository, writeRepository);
    }

    @Test
    void shouldCreateProcessInMemoryAndPersistWriteEntity() {
        repository.create(processLog());

        final ArgumentCaptor<ProcessLogWriteEntity> captor = ArgumentCaptor.forClass(ProcessLogWriteEntity.class);
        verify(writeRepository).save(captor.capture());

        assertThat(captor.getValue().getProcessId()).isEqualTo(processId());
        assertThat(repository.findById(processId())).isPresent();
    }

    @Test
    void shouldFlushProcessToWriteRepositoryWhenItFails() {
        final ProcessLog processLog = processLog();
        processLog.applyShipmentRejected(shipmentRejected("request", null, "failure"));
        repository.create(processLog);

        processLog.changeStatus(ProcessStatus.FAILURE, "failure");
        repository.update(processLog);

        final ArgumentCaptor<ProcessLogWriteEntity> captor = ArgumentCaptor.forClass(ProcessLogWriteEntity.class);
        verify(writeRepository, times(2)).save(captor.capture());

        final ProcessLogWriteEntity failedEntity = captor.getAllValues().getLast();
        assertThat(failedEntity.getStatus()).isEqualTo(ProcessStatus.FAILURE);
        assertThat(failedEntity.getFaultDescription()).isEqualTo("failure");
        assertThat(failedEntity.getCommunicationLogs()).hasSize(1);
    }
}
