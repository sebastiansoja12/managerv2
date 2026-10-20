package com.warehouse.asyncjob.infrastructure.adapter.primary;

import com.warehouse.asyncjob.api.dto.ReadModelRebuildJobDto;
import com.warehouse.asyncjob.application.port.primary.AsyncJobPort;
import com.warehouse.asyncjob.application.port.primary.command.CreateReadModelRebuildJobCommand;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.asyncjob.domain.exception.AsyncJobNotFoundException;
import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.vo.AsyncJobId;
import com.warehouse.asyncjob.domain.vo.AsyncJobSnapshot;
import com.warehouse.asyncjob.infrastructure.adapter.primary.mapper.AsyncJobResponseMapper;
import com.warehouse.commonassets.identificator.OperatorId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ReadModelRebuildApiServiceAdapterTest {

    private static final LocalDate FROM = LocalDate.of(2026, 10, 1);
    private static final LocalDate TO = LocalDate.of(2026, 10, 17);

    private final AsyncJobPort asyncJobPort = mock(AsyncJobPort.class);
    private final ReadModelRebuildApiServiceAdapter adapter =
            new ReadModelRebuildApiServiceAdapter(asyncJobPort, new AsyncJobResponseMapper());

    @Test
    void shouldExposeAllReadModelTypes() {
        assertThat(adapter.readModelTypes()).containsExactly("SHIPMENT", "DEPARTMENT", "DELIVERY");
    }

    @Test
    void shouldStartRebuildForOperator() {
        final AsyncJobSnapshot job = AsyncJob.pending(ReadModelType.SHIPMENT, OperatorId.of(20L), FROM, TO).snapshot();
        when(asyncJobPort.createReadModelRebuildJob(new CreateReadModelRebuildJobCommand(ReadModelType.SHIPMENT,
                OperatorId.of(20L), FROM, TO))).thenReturn(job);

        final ReadModelRebuildJobDto dto = adapter.start("SHIPMENT", 20L, FROM, TO);

        assertThat(dto.jobId()).isEqualTo(job.id().value());
        assertThat(dto.status()).isEqualTo("PENDING");
        assertThat(dto.operatorId()).isEqualTo(20L);
    }

    @Test
    void shouldRejectInvertedDateRange() {
        assertThrows(IllegalArgumentException.class, () -> adapter.start("SHIPMENT", 20L, TO, FROM));
        verifyNoInteractions(asyncJobPort);
    }

    @Test
    void shouldRejectUnknownReadModelType() {
        assertThrows(IllegalArgumentException.class, () -> adapter.start("PARCEL", 20L, FROM, TO));
    }

    @Test
    void shouldReturnEmptyForUnknownJob() {
        final UUID jobId = UUID.randomUUID();
        when(asyncJobPort.get(new AsyncJobId(jobId))).thenThrow(new AsyncJobNotFoundException(new AsyncJobId(jobId)));

        assertThat(adapter.find(jobId)).isEmpty();
    }

    @Test
    void shouldReturnLatestJobsOfOperator() {
        when(asyncJobPort.findLatest(OperatorId.of(20L), 10)).thenReturn(List.of(
                AsyncJob.pending(ReadModelType.DEPARTMENT, OperatorId.of(20L), FROM, TO).snapshot()));

        assertThat(adapter.findLatest(20L, 10)).extracting(ReadModelRebuildJobDto::readModelType)
                .containsExactly("DEPARTMENT");
    }
}
