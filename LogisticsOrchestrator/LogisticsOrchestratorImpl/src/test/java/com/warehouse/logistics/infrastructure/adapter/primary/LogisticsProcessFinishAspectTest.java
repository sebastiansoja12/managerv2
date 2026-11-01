package com.warehouse.logistics.infrastructure.adapter.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.dto.ProcessStatusDto;
import com.warehouse.process.infrastructure.event.ProcessFinishEvent;
import com.warehouse.terminal.jaxb.AcceptResponse;
import com.warehouse.terminal.jaxb.CollectResponse;
import com.warehouse.terminal.jaxb.HandoverResponse;
import com.warehouse.terminal.jaxb.HandoverResponseDetailType;
import com.warehouse.terminal.jaxb.HandoverResponseDetailsType;
import com.warehouse.terminal.jaxb.PalletUnloadResponse;

class LogisticsProcessFinishAspectTest {

    private static final String PROCESS_ID = "5a0b7c1d-2e3f-4a5b-8c6d-7e8f9a0b1c2d";

    private final ProcessHubEventPublisher processHubEventPublisher = mock(ProcessHubEventPublisher.class);

    private final LogisticsProcessFinishAspect aspect = new LogisticsProcessFinishAspect(processHubEventPublisher);

    @Test
    void shouldFinishSuccessfulHandoverProcess() {
        aspect.publishProcessFinishEvent(handoverResponse(true, null));

        final ProcessFinishEvent event = publishedEvent();
        assertThat(event.getProcessStatus()).isEqualTo(ProcessStatusDto.SUCCESS);
        assertThat(event.getFaultDescription()).isNull();
    }

    @Test
    void shouldFinishFailedHandoverProcessWithFirstShipmentError() {
        aspect.publishProcessFinishEvent(handoverResponse(false, "Recipient signature is required for this delivery"));

        final ProcessFinishEvent event = publishedEvent();
        assertThat(event.getProcessStatus()).isEqualTo(ProcessStatusDto.FAILURE);
        assertThat(event.getFaultDescription()).isEqualTo("Recipient signature is required for this delivery");
    }

    @Test
    void shouldFinishFailedPalletUnloadProcessWithErrorMessage() {
        final PalletUnloadResponse response = new PalletUnloadResponse();
        response.setProcessId(PROCESS_ID);
        response.setSuccess(false);
        response.setErrorMessage("Pallet must be unloaded in its destination department");

        aspect.publishProcessFinishEvent(response);

        final ProcessFinishEvent event = publishedEvent();
        assertThat(event.getProcessStatus()).isEqualTo(ProcessStatusDto.FAILURE);
        assertThat(event.getFaultDescription()).isEqualTo("Pallet must be unloaded in its destination department");
    }

    @Test
    void shouldFinishFailedCollectProcessWithDefaultFaultWhenNoErrorMessage() {
        final CollectResponse response = new CollectResponse();
        response.setProcessId(PROCESS_ID);
        response.setSuccess(false);

        aspect.publishProcessFinishEvent(response);

        final ProcessFinishEvent event = publishedEvent();
        assertThat(event.getProcessStatus()).isEqualTo(ProcessStatusDto.FAILURE);
        assertThat(event.getFaultDescription()).isEqualTo("Process failed without error message");
    }

    @Test
    void shouldFinishSuccessfulAcceptProcess() {
        final AcceptResponse response = new AcceptResponse();
        response.setProcessId(PROCESS_ID);
        response.setSuccess(true);

        aspect.publishProcessFinishEvent(response);

        final ProcessFinishEvent event = publishedEvent();
        assertThat(event.getProcessStatus()).isEqualTo(ProcessStatusDto.SUCCESS);
        assertThat(event.getFaultDescription()).isNull();
    }

    private ProcessFinishEvent publishedEvent() {
        final ArgumentCaptor<ProcessFinishEvent> event = ArgumentCaptor.forClass(ProcessFinishEvent.class);
        verify(processHubEventPublisher).publish(event.capture());
        assertThat(event.getValue().getProcessLogId().value().toString()).isEqualTo(PROCESS_ID);
        return event.getValue();
    }

    private HandoverResponse handoverResponse(final boolean success, final String errorMessage) {
        final HandoverResponseDetailType detail = new HandoverResponseDetailType();
        detail.setTrackingNumber("TRK-001");
        detail.setSuccess(success);
        detail.setErrorMessage(errorMessage);
        final HandoverResponseDetailsType details = new HandoverResponseDetailsType();
        details.getHandoverResponseDetail().add(detail);
        final HandoverResponse response = new HandoverResponse();
        response.setProcessId(PROCESS_ID);
        response.setSuccess(success);
        response.setErrorMessage(errorMessage);
        response.setHandoverResponseDetails(details);
        return response;
    }
}
