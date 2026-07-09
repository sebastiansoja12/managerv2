package com.warehouse.process.infrastructure.adapter.secondary.mapper;

import com.warehouse.process.domain.model.CommunicationLogDetail;
import com.warehouse.process.domain.model.DeviceInformation;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.infrastructure.adapter.secondary.entity.DeviceInformationEmbeddable;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.CommunicationLogReadEntity;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.ProcessLogReadEntity;

public abstract class ProcessLogToReadEntityMapper {

    public static ProcessLogReadEntity map(final ProcessLog processLog) {
        final ProcessLogReadEntity parent = ProcessLogReadEntity.builder()
                .faultDescription(processLog.getFaultDescription())
                .processId(processLog.getProcessId())
                .request(processLog.getRequest())
                .response(processLog.getResponse())
                .status(processLog.getStatus())
                .deviceInformation(map(processLog.getDeviceInformation()))
                .createdAt(processLog.getCreatedAt())
                .modifiedAt(processLog.getModifiedAt())
                .build();

        processLog.getCommunicationLogDetails()
                .getCommunicationLogDetails()
                .stream()
                .map(detail -> map(detail, parent))
                .forEach(parent::addCommunicationLog);

        return parent;
    }

    public static CommunicationLogReadEntity map(final CommunicationLogDetail detail,
                                                 final ProcessLogReadEntity processLog) {
        return CommunicationLogReadEntity.builder()
                .processLog(processLog)
                .communicationLogId(detail.getCommunicationLogId())
                .serviceType(detail.getServiceType())
                .processType(detail.getProcessType())
                .createdAt(detail.getCreatedAt())
                .updatedAt(detail.getUpdatedAt())
                .request(detail.getRequest())
                .response(detail.getResponse())
                .deviceId(detail.getDeviceId())
                .departmentCode(detail.getDepartmentCode())
                .faultDescription(detail.getFaultDescription())
                .sourceService(detail.getSourceService())
                .targetService(detail.getTargetService())
                .createdBy(detail.getCreatedBy())
                .updatedBy(detail.getUpdatedBy())
                .build();
    }

    private static DeviceInformationEmbeddable map(final DeviceInformation deviceInformation) {
        return ProcessLogToEntityMapper.map(deviceInformation);
    }
}
