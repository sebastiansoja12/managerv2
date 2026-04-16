package com.warehouse.logistics.infrastructure.adapter.primary;

import java.time.LocalDateTime;
import java.util.UUID;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.dto.ProcessStatusDto;
import com.warehouse.process.infrastructure.event.ProcessFinishEvent;
import com.warehouse.terminal.response.TerminalResponse;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
public class LogisticsProcessFinishAspect {

    private final ProcessHubEventPublisher processHubEventPublisher;

    public LogisticsProcessFinishAspect(final ProcessHubEventPublisher processHubEventPublisher) {
        this.processHubEventPublisher = processHubEventPublisher;
    }

    @AfterReturning(
            pointcut = "@annotation(com.warehouse.commonassets.validator.DeviceAccessValidator)",
            returning = "terminalResponse")
    public void publishProcessFinishEvent(final TerminalResponse terminalResponse) {
        try {
            if (terminalResponse == null || terminalResponse.getProcessId() == null || terminalResponse.getProcessId().isBlank()) {
                return;
            }
            final String faultDescription = resolveFaultDescription(terminalResponse);
            processHubEventPublisher.publish(new ProcessFinishEvent(
                    new ProcessId(UUID.fromString(terminalResponse.getProcessId())),
                    ServiceType.LOGISTICS_ORCHESTRATOR,
                    faultDescription == null ? ProcessStatusDto.SUCCESS : ProcessStatusDto.FAILURE,
                    LocalDateTime.now(),
                    faultDescription));
        } finally {
            LogisticsProcessContext.clear();
        }
    }

    private String resolveFaultDescription(final TerminalResponse terminalResponse) {
        if (terminalResponse.getDeliveryReturnResponse() == null
                || terminalResponse.getDeliveryReturnResponse().getDeliveryReturnResponseDetails() == null) {
            return null;
        }
        return terminalResponse.getDeliveryReturnResponse()
                .getDeliveryReturnResponseDetails()
                .stream()
                .filter(detail -> detail.getErrorMessage() != null && !detail.getErrorMessage().isBlank())
                .map(detail -> detail.getErrorMessage())
                .findFirst()
                .orElse(null);
    }

    @AfterThrowing(
            pointcut = "@annotation(com.warehouse.commonassets.validator.DeviceAccessValidator)",
            throwing = "exception")
    public void publishProcessFailureEvent(final Throwable exception) {
        try {
            final ProcessId processId = LogisticsProcessContext.getProcessId();
            log.error("Logistics SOAP process failed. processId={}, message={}",
                    processId != null ? processId.value() : "uninitialized",
                    exception.getMessage(),
                    exception);
            if (processId == null) {
                return;
            }
            processHubEventPublisher.publish(new ProcessFinishEvent(
                    processId,
                    ServiceType.LOGISTICS_ORCHESTRATOR,
                    ProcessStatusDto.FAILURE,
                    LocalDateTime.now(),
                    exception.getMessage()));
        } finally {
            LogisticsProcessContext.clear();
        }
    }
}
