package com.warehouse.mcp.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.mcp.application.port.secondary.ProcessServicePort;
import com.warehouse.mcp.domain.vo.ProcessDetails;
import com.warehouse.mcp.domain.vo.ProcessStep;
import com.warehouse.process.ProcessHubApiService;
import com.warehouse.process.infrastructure.dto.DepartmentCodeDto;
import com.warehouse.process.infrastructure.dto.ProcessDetailsDto;
import com.warehouse.process.infrastructure.dto.ProcessIdDto;
import com.warehouse.process.infrastructure.dto.ProcessStepDto;

public class ProcessServiceAdapter implements ProcessServicePort {

    private final ProcessHubApiService processHubApiService;

    public ProcessServiceAdapter(final ProcessHubApiService processHubApiService) {
        this.processHubApiService = processHubApiService;
    }

    @Override
    public ProcessDetails findProcess(final ProcessId processId) {
        final ProcessDetailsDto process = this.processHubApiService.getProcess(new ProcessIdDto(processId.value()));
        return new ProcessDetails(
                new ProcessId(process.processId().value()),
                process.status() != null ? process.status().name() : null,
                process.createdAt(),
                process.modifiedAt(),
                process.faultDescription(),
                process.deviceId() != null ? process.deviceId().value() : null,
                value(process.departmentCode()),
                process.userId() != null ? process.userId().value() : null,
                process.steps().stream().map(this::map).toList());
    }

    private ProcessStep map(final ProcessStepDto step) {
        return new ProcessStep(step.createdAt(), step.processType(), step.serviceType(), step.sourceService(),
                step.targetService(), value(step.departmentCode()), step.faultDescription());
    }

    private String value(final DepartmentCodeDto departmentCode) {
        return departmentCode != null ? departmentCode.value() : null;
    }
}
