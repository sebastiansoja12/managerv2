package com.warehouse.terminal.infrastructure.adapter.primary;

import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.service.TerminalValidatorService;
import com.warehouse.terminal.dto.DeviceValidationRequestDto;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.terminal.event.DeviceEvent;
import com.warehouse.terminal.event.DeviceValidationEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DeviceValidatorListener {

    private final TerminalValidatorService terminalValidatorService;

    public DeviceValidatorListener(final TerminalValidatorService terminalValidatorService) {
        this.terminalValidatorService = terminalValidatorService;
    }

    @EventListener
    public void handle(final DeviceValidationEvent event) {
        logEvent(event);
        final DeviceValidationRequestDto deviceValidationRequest = event.getDeviceValidationRequest();
        final Terminal terminal = Terminal.from(deviceValidationRequest);
        terminalValidatorService.validateDevice(terminal);
    }

    private void logEvent(final DeviceEvent event) {
        log.info("Detected event: {} at {}", event.getClass().getSimpleName(), event.getTimestamp());
    }
}
