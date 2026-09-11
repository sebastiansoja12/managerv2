package com.warehouse.logistics.infrastructure.adapter.secondary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.logistics.application.port.secondary.DeviceValidateCommand;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.event.ProcessDeviceValidatedEvent;
import com.warehouse.terminal.DeviceEventPublisher;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.DeviceUserType;

class DeviceAgentServiceAdapterTest {

    private static final ProcessId PROCESS_ID = new ProcessId(UUID.fromString("5a0b7c1d-2e3f-4a5b-8c6d-7e8f9a0b1c2d"));

    private final DeviceEventPublisher deviceEventPublisher = mock(DeviceEventPublisher.class);
    private final ProcessHubEventPublisher processHubEventPublisher = mock(ProcessHubEventPublisher.class);
    private final DeviceAgentServiceAdapter adapter = new DeviceAgentServiceAdapter(deviceEventPublisher,
            processHubEventPublisher);

    @Test
    void shouldRecordSuccessfulDeviceValidationInProcess() {
        adapter.validateDevice(command());

        final ProcessDeviceValidatedEvent event = publishedEvent();
        assertThat(event.getRequest()).contains("deviceId=MB:1", "username=courier");
        assertThat(event.getResponse()).isEqualTo("DeviceValidationResponse{valid=true}");
        assertThat(event.getFaultDescription()).isNull();
    }

    @Test
    void shouldRecordFailedDeviceValidationInProcessAndRethrow() {
        doThrow(new IllegalStateException("Device is not valid:MB:1")).when(deviceEventPublisher).send(any());

        assertThatThrownBy(() -> adapter.validateDevice(command()))
                .isInstanceOf(IllegalStateException.class);

        final ProcessDeviceValidatedEvent event = publishedEvent();
        assertThat(event.getFaultDescription()).isEqualTo("Device is not valid:MB:1");
        assertThat(event.getResponse()).isNull();
    }

    private ProcessDeviceValidatedEvent publishedEvent() {
        final ArgumentCaptor<ProcessDeviceValidatedEvent> event = ArgumentCaptor.forClass(ProcessDeviceValidatedEvent.class);
        verify(processHubEventPublisher).publish(event.capture());
        assertThat(event.getValue().getProcessId()).isEqualTo(PROCESS_ID);
        assertThat(event.getValue().getProcessType()).isEqualTo(ProcessType.COLLECT);
        return event.getValue();
    }

    private DeviceValidateCommand command() {
        return new DeviceValidateCommand(PROCESS_ID, ProcessType.COLLECT, DeviceInformation.builder()
                .deviceId(new DeviceId("MB:1"))
                .departmentCode(new DepartmentCode("KT1"))
                .deviceType(DeviceType.MOBILE)
                .deviceUserType(DeviceUserType.SUPPLIER)
                .version("1.0.0")
                .username("courier")
                .build());
    }
}
