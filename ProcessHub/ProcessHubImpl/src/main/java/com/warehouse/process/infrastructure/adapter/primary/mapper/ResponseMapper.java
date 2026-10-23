package com.warehouse.process.infrastructure.adapter.primary.mapper;

import java.util.Comparator;
import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.model.CommunicationLogDetail;
import com.warehouse.process.domain.model.DeviceInformation;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.infrastructure.dto.DepartmentCodeDto;
import com.warehouse.process.infrastructure.dto.DeviceIdDto;
import com.warehouse.process.infrastructure.dto.ProcessDetailsDto;
import com.warehouse.process.infrastructure.dto.ProcessIdDto;
import com.warehouse.process.infrastructure.dto.ProcessStatusDto;
import com.warehouse.process.infrastructure.dto.ProcessStepDto;
import com.warehouse.process.infrastructure.dto.UserIdDto;

public abstract class ResponseMapper {

    public static ProcessIdDto map(final ProcessId processId) {
        return new ProcessIdDto(processId.value());
    }

    public static ProcessDetailsDto map(final ProcessLog processLog) {
        final DeviceInformation deviceInformation = processLog.getDeviceInformation();
        return new ProcessDetailsDto(
                map(processLog.getProcessId()),
                processLog.getStatus() != null ? ProcessStatusDto.valueOf(processLog.getStatus().name()) : null,
                processLog.getCreatedAt(),
                processLog.getModifiedAt(),
                processLog.getFaultDescription(),
                deviceInformation != null && deviceInformation.deviceId() != null
                        ? new DeviceIdDto(deviceInformation.deviceId().value())
                        : null,
                deviceInformation != null ? map(deviceInformation.departmentCode()) : null,
                deviceInformation != null && deviceInformation.userId() != null
                        ? new UserIdDto(deviceInformation.userId().value())
                        : null,
                mapSteps(processLog));
    }

    private static List<ProcessStepDto> mapSteps(final ProcessLog processLog) {
        return processLog.getCommunicationLogDetails()
                .getCommunicationLogDetails()
                .stream()
                .sorted(Comparator.comparing(CommunicationLogDetail::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .map(detail -> new ProcessStepDto(
                        detail.getCreatedAt(),
                        detail.getProcessType(),
                        detail.getServiceType(),
                        detail.getSourceService(),
                        detail.getTargetService(),
                        map(detail.getDepartmentCode()),
                        detail.getFaultDescription()))
                .toList();
    }

    private static DepartmentCodeDto map(final DepartmentCode departmentCode) {
        return departmentCode != null ? new DepartmentCodeDto(departmentCode.value()) : null;
    }
}
