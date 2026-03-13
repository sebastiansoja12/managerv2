package com.warehouse.process.domain.port.primary;

import static com.warehouse.process.ProcessHubTestFixtures.initializeCommand;
import static com.warehouse.process.ProcessHubTestFixtures.initializeDomainContext;
import static com.warehouse.process.ProcessHubTestFixtures.processId;
import static com.warehouse.process.ProcessHubTestFixtures.processLog;
import static com.warehouse.process.ProcessHubTestFixtures.shipmentRejected;
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

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.port.secondary.ProcessRepository;
import com.warehouse.process.domain.service.ProcessServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProcessPortImplTest {

    @Mock
    private ProcessRepository processRepository;

    private ProcessPort processPort;

    @BeforeEach
    void setUp() {
        initializeDomainContext();
        processPort = new ProcessPortImpl(new ProcessServiceImpl(processRepository));
    }

    @Test
    void shouldInitializeProcessUsingRealServiceAndRepositoryPort() {
        final ProcessId id = processPort.initialize(initializeCommand());

        final ArgumentCaptor<ProcessLog> captor = ArgumentCaptor.forClass(ProcessLog.class);
        verify(processRepository).create(captor.capture());

        assertThat(id).isNotNull();
        assertThat(captor.getValue().getProcessId()).isEqualTo(id);
        assertThat(captor.getValue().getRequest()).isEqualTo("<TerminalRequest/>");
        assertThat(captor.getValue().getDeviceInformation()).isNotNull();
        assertThat(captor.getValue().getCreatedAt()).isNotNull();
        assertThat(captor.getValue().getModifiedAt()).isNotNull();
    }

    @Test
    void shouldFinishProcessAsFailureWithFaultDescription() {
        final ProcessLog processLog = processLog();
        when(processRepository.findById(processId())).thenReturn(Optional.of(processLog));

        processPort.finishProcess(processId(), ProcessStatus.FAILURE, "shipment manager unavailable");

        final ArgumentCaptor<ProcessLog> captor = ArgumentCaptor.forClass(ProcessLog.class);
        verify(processRepository).update(captor.capture());

        assertThat(captor.getValue().getStatus()).isEqualTo(ProcessStatus.FAILURE);
        assertThat(captor.getValue().getFaultDescription()).isEqualTo("shipment manager unavailable");
    }

    @Test
    void shouldAssignShipmentRejectedAsNewCommunicationLog() {
        final ProcessLog processLog = processLog();
        when(processRepository.findById(processId())).thenReturn(Optional.of(processLog));

        processPort.assignShipmentRejected(processId(), shipmentRejected("request", "response", null));

        final ArgumentCaptor<ProcessLog> captor = ArgumentCaptor.forClass(ProcessLog.class);
        verify(processRepository).update(captor.capture());

        assertThat(captor.getValue().getCommunicationLogDetails().getCommunicationLogDetails())
                .hasSize(1)
                .extracting(detail -> detail.getRequest() + "|" + detail.getResponse())
                .containsExactly("request|response");
    }
}
