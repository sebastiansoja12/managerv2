package com.warehouse.asyncjob.infrastructure.adapter.primary;

import com.warehouse.asyncjob.api.dto.ReadModelRebuildJobDto;
import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.model.ReadModelType;
import com.warehouse.asyncjob.domain.service.AsyncJobNotFoundException;
import com.warehouse.asyncjob.domain.service.AsyncJobService;
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

    private final AsyncJobService asyncJobService = mock(AsyncJobService.class);

    private final ReadModelRebuildApiServiceAdapter adapter = new ReadModelRebuildApiServiceAdapter(asyncJobService);

    @Test
    void shouldExposeAllReadModelTypes() {
        assertThat(adapter.readModelTypes()).containsExactly("SHIPMENT", "DEPARTMENT");
    }

    @Test
    void shouldStartRebuildForOperator() {
        final AsyncJob job = AsyncJob.pending(ReadModelType.SHIPMENT, 20L, FROM, TO);
        when(asyncJobService.createReadModelRebuildJob(ReadModelType.SHIPMENT, 20L, FROM, TO)).thenReturn(job);

        final ReadModelRebuildJobDto dto = adapter.start("SHIPMENT", 20L, FROM, TO);

        assertThat(dto.jobId()).isEqualTo(job.getId());
        assertThat(dto.status()).isEqualTo("PENDING");
        assertThat(dto.operatorId()).isEqualTo(20L);
    }

    @Test
    void shouldRejectInvertedDateRange() {
        assertThrows(IllegalArgumentException.class, () -> adapter.start("SHIPMENT", 20L, TO, FROM));

        verifyNoInteractions(asyncJobService);
    }

    @Test
    void shouldRejectUnknownReadModelType() {
        assertThrows(IllegalArgumentException.class, () -> adapter.start("PARCEL", 20L, FROM, TO));
    }

    @Test
    void shouldReturnEmptyForUnknownJob() {
        final UUID jobId = UUID.randomUUID();
        when(asyncJobService.get(jobId)).thenThrow(new AsyncJobNotFoundException(jobId));

        assertThat(adapter.find(jobId)).isEmpty();
    }

    @Test
    void shouldReturnLatestJobsOfOperator() {
        when(asyncJobService.findLatest(20L, 10)).thenReturn(List.of(AsyncJob.pending(ReadModelType.DEPARTMENT, 20L,
                FROM, TO)));

        assertThat(adapter.findLatest(20L, 10)).extracting(ReadModelRebuildJobDto::readModelType)
                .containsExactly("DEPARTMENT");
    }
}
