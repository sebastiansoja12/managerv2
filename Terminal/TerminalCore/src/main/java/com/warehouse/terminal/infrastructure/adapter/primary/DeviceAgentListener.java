package com.warehouse.terminal.infrastructure.adapter.primary;

import com.warehouse.terminal.domain.model.command.DeviceSettingsRequest;
import com.warehouse.terminal.domain.vo.DeviceValidationRequest;
import com.warehouse.terminal.domain.vo.DeviceVersionRequest;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.terminal.domain.port.primary.DevicePort;
import com.warehouse.terminal.domain.service.DeviceValidatorConfigurationService;
import com.warehouse.terminal.domain.service.DeviceValidatorService;
import com.warehouse.terminal.dto.DeviceSettingsRequestDto;
import com.warehouse.terminal.dto.DeviceUpdateRequestDto;
import com.warehouse.terminal.event.DeviceEvent;
import com.warehouse.terminal.event.DeviceSettingsEvent;
import com.warehouse.terminal.event.DeviceUpdateEvent;
import com.warehouse.terminal.event.DeviceValidationEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DeviceAgentListener {

    private final DeviceValidatorService deviceValidatorService;

    private final DeviceValidatorConfigurationService deviceValidatorConfiguration;

    private final DevicePort devicePort;

    public DeviceAgentListener(final DeviceValidatorService deviceValidatorService,
                               final DeviceValidatorConfigurationService deviceValidatorConfiguration,
                               final DevicePort devicePort) {
        this.deviceValidatorService = deviceValidatorService;
        this.deviceValidatorConfiguration = deviceValidatorConfiguration;
        this.devicePort = devicePort;
    }

    @EventListener
    public void handle(final DeviceValidationEvent event) {
        logEvent(event);
        final DeviceValidationRequest deviceValidationRequest = DeviceValidationRequest.from(event);
        deviceValidatorService.validateDevice(deviceValidationRequest);
    }

    @EventListener
    public void handle(final DeviceUpdateEvent event) {
        logEvent(event);
        final DeviceUpdateRequestDto deviceUpdateRequest = event.getDeviceUpdateRequest();
        devicePort.changeVersionTo(DeviceVersionRequest.from(deviceUpdateRequest));
    }

    @EventListener
    public void handle(final DeviceSettingsEvent event) {
        logEvent(event);
        final DeviceSettingsRequestDto deviceSettingsRequest = event.getRequest();
        devicePort.updateSettings(DeviceSettingsRequest.from(deviceSettingsRequest));
    }

    private void logEvent(final DeviceEvent event) {
        log.info("Detected event: {} at {}", event.getClass().getSimpleName(), event.getTimestamp());
    }
}
