package com.warehouse.process.domain.service;

import static com.warehouse.process.ProcessHubTestFixtures.initializeDomainContext;
import static com.warehouse.process.ProcessHubTestFixtures.processCommunication;
import static com.warehouse.process.ProcessHubTestFixtures.processId;
import static com.warehouse.process.ProcessHubTestFixtures.processLog;
import static com.warehouse.process.ProcessHubTestFixtures.shipmentUpdated;
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

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.port.secondary.ProcessRepository;

@ExtendWith(MockitoExtension.class)
class ProcessServiceImplTest {

    @Mock
    private ProcessRepository processRepository;

    private ProcessServiceImpl processService;

    @BeforeEach
    void setUp() {
        initializeDomainContext();
        processService = new ProcessServiceImpl(processRepository);
    }

    @Test
    void shouldCreateProcessAndPublishDomainEvent() {
        final ProcessLog processLog = processLog();

        processService.createProcess(processLog);

        verify(processRepository).create(processLog);
    }

    @Test
    void shouldAppendCommunicationLogForEachShipmentUpdateEvent() {
        final ProcessLog processLog = processLog();
        when(processRepository.findById(processId())).thenReturn(Optional.of(processLog));

        processService.assignShipmentUpdated(processId(), shipmentUpdated("request-1", "response-1"));
        processService.assignShipmentUpdated(processId(), shipmentUpdated("request-2", "response-2"));

        final ArgumentCaptor<ProcessLog> captor = ArgumentCaptor.forClass(ProcessLog.class);
        verify(processRepository, org.mockito.Mockito.times(2)).update(captor.capture());

        final ProcessLog updated = captor.getAllValues().getLast();
        assertThat(updated.getCommunicationLogDetails().getCommunicationLogDetails())
                .hasSize(2)
                .extracting(detail -> detail.getRequest())
                .containsExactlyInAnyOrder("request-1", "request-2");
    }

    @Test
    void shouldMarkProcessAsFailureWithFaultDescription() {
        final ProcessLog processLog = processLog();
        when(processRepository.findById(processId())).thenReturn(Optional.of(processLog));

        processService.logFailedProcess(processId(), "NPE while processing terminal request");

        final ArgumentCaptor<ProcessLog> captor = ArgumentCaptor.forClass(ProcessLog.class);
        verify(processRepository).update(captor.capture());

        assertThat(captor.getValue().getStatus()).isEqualTo(ProcessStatus.FAILURE);
        assertThat(captor.getValue().getFaultDescription()).isEqualTo("NPE while processing terminal request");
    }

    @Test
    void shouldAppendCommunicationLogForEachServiceCommunication() {
        final ProcessLog processLog = processLog();
        when(processRepository.findById(processId())).thenReturn(Optional.of(processLog));

        processService.assignCommunication(processId(),
                processCommunication(ServiceType.RETURNING_TRACK_MANAGER, "token-request", "token-response", null));
        processService.assignCommunication(processId(),
                processCommunication(ServiceType.SHIPMENT_MANAGEMENT, "shipment-request", "shipment-response", null));

        final ArgumentCaptor<ProcessLog> captor = ArgumentCaptor.forClass(ProcessLog.class);
        verify(processRepository, org.mockito.Mockito.times(2)).update(captor.capture());

        final ProcessLog updated = captor.getAllValues().getLast();
        assertThat(updated.getCommunicationLogDetails().getCommunicationLogDetails())
                .hasSize(2)
                .extracting(detail -> detail.getTargetService() + "|" + detail.getRequest())
                .containsExactlyInAnyOrder(
                        "RETURNING_TRACK_MANAGER|token-request",
                        "SHIPMENT_MANAGEMENT|shipment-request");
    }
}
