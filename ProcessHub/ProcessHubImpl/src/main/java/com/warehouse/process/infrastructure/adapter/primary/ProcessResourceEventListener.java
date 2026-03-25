package com.warehouse.process.infrastructure.adapter.primary;

import java.time.format.DateTimeFormatter;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.model.ProcessDeviceValidatedCommand;
import com.warehouse.process.domain.port.primary.ProcessPort;
import com.warehouse.process.domain.vo.ChangeResponseProcessCommand;
import com.warehouse.process.domain.vo.ProcessCommunication;
import com.warehouse.process.domain.vo.ShipmentRejected;
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

    private final CurrentOperatorService currentOperatorService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSSS");

    public ProcessResourceEventListener(final ProcessPort processPort, final CurrentOperatorService currentOperatorService) {
        this.processPort = processPort;
        this.currentOperatorService = currentOperatorService;
    }

    @EventListener
    public void handle(final ProcessFinishEvent event) {
        logEvent(event);
        final ProcessStatus processStatus = RequestMapper.map(event.getProcessStatus());
        final ProcessId processId = RequestMapper.map(event.getProcessLogId());
        this.processPort.finishProcess(processId, processStatus, event.getFaultDescription());
    }

    @EventListener
    public void handle(final ProcessShipmentUpdatedEvent event) {
        logEvent(event);
        final ProcessId processId = RequestMapper.map(event.getProcessLogId());
        final ShipmentUpdateDto shipmentUpdate = event.getShipmentUpdate();
        final ShipmentUpdated shipmentUpdated = RequestMapper.map(shipmentUpdate, currentOperatorService.getCurrentOperatorId());
        this.processPort.assignShipmentUpdated(processId, shipmentUpdated);
    }

    @EventListener
    public void handle(final ProcessShipmentRejectedEvent event) {
        if (event instanceof ProcessShipmentRejectedFailedEvent) {
            return;
        }
        logEvent(event);
        final ProcessId processId = RequestMapper.map(event.getProcessLogId());
        this.processPort.assignShipmentRejected(processId, new ShipmentRejected(
                event.getServiceType(),
                com.warehouse.commonassets.enumeration.ProcessType.REJECT,
                event.getRequest(),
                event.getResponse(),
                null
        ));
    }

    @EventListener
    public void handle(final ProcessShipmentRejectedFailedEvent event) {
        logEvent(event);
        final ProcessId processId = RequestMapper.map(event.getProcessLogId());
        this.processPort.assignShipmentRejected(processId, new ShipmentRejected(
                event.getServiceType(),
                com.warehouse.commonassets.enumeration.ProcessType.REJECT,
                event.getRequest(),
                event.getResponse(),
                event.getFaultDescription()
        ));
        this.processPort.finishProcess(processId, ProcessStatus.FAILURE, event.getFaultDescription());
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

    @EventListener
    public void handle(final ProcessCommunicationEvent event) {
        logEvent(event);
        final ProcessId processId = RequestMapper.map(event.getProcessLogId());
        this.processPort.assignCommunication(processId, new ProcessCommunication(
                event.getServiceType(),
                event.getTargetServiceType(),
                event.getProcessType(),
                event.getRequest(),
                event.getResponse(),
                event.getFaultDescription()
        ));
    }

    private void logEvent(final ProcessLogEvent event) {
        log.info("Detected event {} at {}", event.getClass().getSimpleName(),
                event.getCreatedAt().format(FORMATTER));
    }
}
