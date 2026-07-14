package com.warehouse.process.infrastructure.adapter.primary;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.ProcessHubApiService;
import com.warehouse.process.domain.model.InitializeProcessCommand;
import com.warehouse.process.domain.port.primary.ProcessPort;
import com.warehouse.process.infrastructure.adapter.primary.mapper.RequestMapper;
import com.warehouse.process.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.process.infrastructure.dto.InitializeProcessRequestDto;
import com.warehouse.process.infrastructure.dto.ProcessIdDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessResourceAdapter implements ProcessHubApiService {

    private final ProcessPort processPort;

    private final CurrentOperatorService currentOperatorService;

    public ProcessResourceAdapter(final ProcessPort processPort, final CurrentOperatorService currentOperatorService) {
        this.processPort = processPort;
        this.currentOperatorService = currentOperatorService;
    }

    @Override
    public ProcessIdDto initialize(final InitializeProcessRequestDto request) {
        final InitializeProcessCommand command = RequestMapper.map(request, currentOperatorService.getCurrentOperatorId());
        final ProcessId processId = this.processPort.initialize(command);
        return ResponseMapper.map(processId);
    }
}
