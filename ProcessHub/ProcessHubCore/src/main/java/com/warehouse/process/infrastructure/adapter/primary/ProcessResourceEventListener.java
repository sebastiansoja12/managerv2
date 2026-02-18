package com.warehouse.process.infrastructure.adapter.primary;

import java.time.format.DateTimeFormatter;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.model.ProcessDeviceValidatedCommand;
import com.warehouse.process.domain.port.primary.ProcessPort;
import com.warehouse.process.domain.vo.ChangeResponseProcessCommand;
import com.warehouse.process.domain.vo.ShipmentUpdated;
import com.warehouse.process.infrastructure.adapter.primary.mapper.RequestMapper;
import com.warehouse.process.infrastructure.dto.ShipmentUpdateDto;
import com.warehouse.process.infrastructure.event.*;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ControllerAdvice
public class ProcessResourceEventListener {

    private final ProcessPort processPort;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSSS");

    public ProcessResourceEventListener(final ProcessPort processPort) {
        this.processPort = processPort;
    }

    @EventListener
    public void handle(final ProcessFinishEvent event) {
        logEvent(event);
        final ProcessStatus processStatus = RequestMapper.map(event.getProcessStatus());
        final ProcessId processId = RequestMapper.map(event.getProcessLogId());
        this.processPort.finishProcess(processId, processStatus);
    }

    @EventListener
    public void handle(final ProcessShipmentUpdatedEvent event) {
        logEvent(event);
        final ProcessId processId = RequestMapper.map(event.getProcessLogId());
        final ShipmentUpdateDto shipmentUpdate = event.getShipmentUpdate();
        final ShipmentUpdated shipmentUpdated = RequestMapper.map(shipmentUpdate);
        this.processPort.assignShipmentUpdated(processId, shipmentUpdated);
    }

    @EventListener
    public void handle(final ProcessDeviceValidatedEvent event) {
        logEvent(event);
        final ProcessDeviceValidatedCommand command = ProcessDeviceValidatedCommand.builder()
                .deviceId(event.getDeviceId())
                .processId(event.getProcessId())
                .sourceServiceType(event.getSourceServiceType())
                .targetServiceType(event.getTargetServiceType())
                .timestamp(event.getCreatedAt())
                .processType(event.getProcessType())
                .build();
        this.processPort.assignProcessDeviceValidation(command);
    }

    @EventListener
    public void handle(final ProcessResponseChangedEvent event) {
        logEvent(event);
        final ProcessId processId = RequestMapper.map(event.getProcessLogId());
        this.processPort.changeResponse(new ChangeResponseProcessCommand(processId, event.getResponse()));
    }

    private void logEvent(final ProcessLogEvent event) {
        log.info("Detected event {} at {}", event.getClass().getSimpleName(),
                event.getCreatedAt().format(FORMATTER));
    }
}
