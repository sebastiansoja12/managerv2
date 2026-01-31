package com.warehouse.process.infrastructure.adapter.primary;

import com.warehouse.commonassets.exception.ProcessException;
import com.warehouse.commonassets.exception.ProcessFailureDetails;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.port.primary.ProcessPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ProcessFailureHandler {

    private final ProcessPort processPort;

    public ProcessFailureHandler(final ProcessPort processPort) {
        this.processPort = processPort;
    }

    @ExceptionHandler(ProcessException.class)
    public void handle(final ProcessException ex) {
        final ProcessFailureDetails failureDetails = ex.getProcessFailureDetails();
        log.warn("Error occurred at: {} with message {}", failureDetails.getOccurredAt(), failureDetails.getExceptionMessage());
        this.processPort.finishProcess(
                failureDetails.getProcessId(), ProcessStatus.FAILURE
        );
    }
}
