package com.warehouse.terminal.infrastructure.adapter.primary;

import com.warehouse.terminal.domain.vo.DeviceVersionRequest;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.port.primary.TerminalPort;
import com.warehouse.terminal.domain.service.DeviceValidatorConfigurationService;
import com.warehouse.terminal.domain.service.TerminalValidatorService;
import com.warehouse.terminal.dto.DeviceUpdateRequestDto;
import com.warehouse.terminal.dto.DeviceValidationRequestDto;
import com.warehouse.terminal.event.DeviceEvent;
import com.warehouse.terminal.event.DeviceUpdateEvent;
import com.warehouse.terminal.event.DeviceValidationEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DeviceAgentListener {

    private final TerminalValidatorService terminalValidatorService;

    private final DeviceValidatorConfigurationService deviceValidatorConfiguration;

    private final TerminalPort terminalPort;

    public DeviceAgentListener(final TerminalValidatorService terminalValidatorService,
                               final DeviceValidatorConfigurationService deviceValidatorConfiguration,
                               final TerminalPort terminalPort) {
        this.terminalValidatorService = terminalValidatorService;
        this.deviceValidatorConfiguration = deviceValidatorConfiguration;
        this.terminalPort = terminalPort;
    }

    @EventListener
    public void handle(final DeviceValidationEvent event) {
        logEvent(event);
        final DeviceValidationRequestDto deviceValidationRequest = event.getDeviceValidationRequest();
        final Terminal terminal = Terminal.from(deviceValidationRequest);
        if (deviceValidatorConfiguration.validateSoftwareConfiguration(terminal.getDeviceId()).validation()) {
            terminalValidatorService.validateDevice(terminal);
        }
    }

    @EventListener
    public void handle(final DeviceUpdateEvent event) {
        logEvent(event);
        final DeviceUpdateRequestDto deviceUpdateRequest = event.getDeviceUpdateRequest();
        terminalPort.changeVersionTo(DeviceVersionRequest.from(deviceUpdateRequest));
    }

    private void logEvent(final DeviceEvent event) {
        log.info("Detected event: {} at {}", event.getClass().getSimpleName(), event.getTimestamp());
    }
}
