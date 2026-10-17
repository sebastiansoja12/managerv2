package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.primary.command.StartReadModelRebuildCommand;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.ReadModelRebuildServicePort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.exception.ReadModelRebuildJobNotFoundException;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.domain.vo.ReadModelRebuildJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReadModelRebuildPortImplTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    private static final LocalDate FROM = LocalDate.of(2026, 10, 1);

    private static final LocalDate TO = LocalDate.of(2026, 10, 17);

    private final OperatorServicePort operatorServicePort = mock(OperatorServicePort.class);

    private final ReadModelRebuildServicePort readModelRebuildServicePort = mock(ReadModelRebuildServicePort.class);

    private final ReadModelRebuildPort port = new ReadModelRebuildPortImpl(operatorServicePort, readModelRebuildServicePort);

    @BeforeEach
    void setUp() {
        when(operatorServicePort.findScope(OPERATOR_ID)).thenReturn(Optional.of(new OperatorScope(OPERATOR_ID, new UserId(1L))));
        when(readModelRebuildServicePort.readModelTypes()).thenReturn(List.of("SHIPMENT", "DEPARTMENT"));
    }

    @Test
    void shouldStartRebuildForSelectedOperator() {
        final StartReadModelRebuildCommand command = new StartReadModelRebuildCommand("SHIPMENT", FROM, TO);
        final ReadModelRebuildJob job = job();
        when(readModelRebuildServicePort.start(OPERATOR_ID, command)).thenReturn(job);

        assertThat(port.start(OPERATOR_ID, command)).isEqualTo(job);
    }

    @Test
    void shouldRejectUnknownReadModelType() {
        assertThrows(IllegalArgumentException.class,
                () -> port.start(OPERATOR_ID, new StartReadModelRebuildCommand("PARCEL", FROM, TO)));

        verify(readModelRebuildServicePort, never()).start(any(), any());
    }

    @Test
    void shouldRejectInvertedDateRange() {
        assertThrows(IllegalArgumentException.class,
                () -> port.start(OPERATOR_ID, new StartReadModelRebuildCommand("SHIPMENT", TO, FROM)));
    }

    @Test
    void shouldRejectRangeLongerThanYear() {
        assertThrows(IllegalArgumentException.class, () -> port.start(OPERATOR_ID,
                new StartReadModelRebuildCommand("SHIPMENT", LocalDate.of(2025, 1, 1), LocalDate.of(2026, 10, 17))));
    }

    @Test
    void shouldRejectRebuildForUnknownOperator() {
        when(operatorServicePort.findScope(OPERATOR_ID)).thenReturn(Optional.empty());

        assertThrows(OperatorNotFoundException.class,
                () -> port.start(OPERATOR_ID, new StartReadModelRebuildCommand("SHIPMENT", FROM, TO)));
    }

    @Test
    void shouldFailForUnknownJob() {
        final UUID jobId = UUID.randomUUID();
        when(readModelRebuildServicePort.find(jobId)).thenReturn(Optional.empty());

        assertThrows(ReadModelRebuildJobNotFoundException.class, () -> port.find(jobId));
    }

    @Test
    void shouldReturnLatestJobsOfOperator() {
        when(readModelRebuildServicePort.findLatest(OPERATOR_ID, ReadModelRebuildPortImpl.LATEST_JOBS_LIMIT))
                .thenReturn(List.of(job()));

        assertThat(port.findLatest(OPERATOR_ID)).hasSize(1);
    }

    private ReadModelRebuildJob job() {
        return new ReadModelRebuildJob(UUID.randomUUID(), "SHIPMENT", "PENDING", FROM, TO, OPERATOR_ID, null, null,
                null, null);
    }
}
