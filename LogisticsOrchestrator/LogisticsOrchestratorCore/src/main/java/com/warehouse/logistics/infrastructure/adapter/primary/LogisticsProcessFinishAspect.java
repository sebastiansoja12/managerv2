package com.warehouse.logistics.infrastructure.adapter.primary;

import java.time.LocalDateTime;
import java.util.UUID;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.dto.ProcessStatusDto;
import com.warehouse.process.infrastructure.event.ProcessFinishEvent;
import com.warehouse.terminal.response.TerminalResponse;

@Aspect
public class LogisticsProcessFinishAspect {

    private final ProcessHubEventPublisher processHubEventPublisher;

    public LogisticsProcessFinishAspect(final ProcessHubEventPublisher processHubEventPublisher) {
        this.processHubEventPublisher = processHubEventPublisher;
    }

    @AfterReturning(
            pointcut = "@annotation(com.warehouse.commonassets.validator.DeviceAccessValidator)",
            returning = "terminalResponse")
    public void publishProcessFinishEvent(final TerminalResponse terminalResponse) {
        if (terminalResponse == null || terminalResponse.getProcessId() == null || terminalResponse.getProcessId().isBlank()) {
            return;
        }
        processHubEventPublisher.publish(new ProcessFinishEvent(
                new ProcessId(UUID.fromString(terminalResponse.getProcessId())),
                ServiceType.LOGISTICS_ORCHESTRATOR,
                ProcessStatusDto.SUCCESS,
                LocalDateTime.now()));
    }
}
