package com.warehouse.process.infrastructure.adapter.secondary.mapper;

import com.warehouse.process.domain.model.CommunicationLogDetail;
import com.warehouse.process.domain.model.DeviceInformation;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.infrastructure.adapter.secondary.entity.CommunicationLogBaseEntity;
import com.warehouse.process.infrastructure.adapter.secondary.entity.DeviceInformationEmbeddable;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.ProcessLogReadEntity;

public abstract class ProcessLogToModelMapper {

    public static ProcessLog map(final ProcessLogReadEntity readEntity) {
        final ProcessLog processLog = ProcessLog.builder()
                .processId(readEntity.getProcessId())
                .request(readEntity.getRequest())
                .response(readEntity.getResponse())
                .status(readEntity.getStatus())
                .faultDescription(readEntity.getFaultDescription())
                .createdAt(readEntity.getCreatedAt())
                .modifiedAt(readEntity.getModifiedAt())
                .deviceInformation(map(readEntity.getDeviceInformation()))
                .build();

        readEntity.getCommunicationLogs()
                .stream()
                .map(ProcessLogToModelMapper::map)
                .forEach(processLog.getCommunicationLogDetails().getCommunicationLogDetails()::add);

        return processLog;
    }

    private static CommunicationLogDetail map(final CommunicationLogBaseEntity entity) {
        return CommunicationLogDetail.builder()
                .communicationLogId(entity.getCommunicationLogId())
                .deviceId(entity.getDeviceId())
                .processType(entity.getProcessType())
                .serviceType(entity.getServiceType())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .departmentCode(entity.getDepartmentCode())
                .sourceService(entity.getSourceService())
                .targetService(entity.getTargetService())
                .request(entity.getRequest())
                .response(entity.getResponse())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .faultDescription(entity.getFaultDescription())
                .build();
    }

    private static DeviceInformation map(final DeviceInformationEmbeddable entity) {
        if (entity == null) {
            return null;
        }

        return new DeviceInformation(
                entity.getDeviceId(),
                entity.getDepartmentCode(),
                entity.getUserId(),
                entity.getDeviceType(),
                entity.getDeviceUserType(),
                entity.getVersion()
        );
    }
}
