package com.warehouse.process.domain.port.primary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.model.InitializeProcessCommand;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.service.ProcessService;

import com.warehouse.process.domain.vo.ChangeResponseProcessCommand;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class ProcessPortImpl implements ProcessPort {

    private final ProcessService processService;

    public ProcessPortImpl(final ProcessService processService) {
        this.processService = processService;
    }

    @Override
    public ProcessId initialize(final InitializeProcessCommand command) {
        final ProcessId processId = this.processService.nextProcessId();
        final ProcessLog processLog = ProcessLog.builder()
                .processId(processId)
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .deviceInformation(command.deviceInformation())
                .request(command.request())
                .build();
        this.processService.createProcess(processLog);
        return processId;
    }

    @Override
    public void changeResponse(final ChangeResponseProcessCommand command) {
        this.processService.updateResponse(command.processId(), command.response());
    }
}
