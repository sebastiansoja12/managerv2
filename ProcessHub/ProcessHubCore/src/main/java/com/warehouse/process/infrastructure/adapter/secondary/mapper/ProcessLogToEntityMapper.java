package com.warehouse.process.infrastructure.adapter.secondary.mapper;

import java.util.HashSet;
import java.util.Set;

import com.warehouse.process.domain.model.CommunicationLogDetail;
import com.warehouse.process.domain.model.DeviceInformation;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.infrastructure.adapter.secondary.entity.DeviceInformationEmbeddable;
import com.warehouse.process.infrastructure.adapter.secondary.entity.write.CommunicationLogWriteEntity;
import com.warehouse.process.infrastructure.adapter.secondary.entity.write.ProcessLogWriteEntity;

public abstract class ProcessLogToEntityMapper {

    public static ProcessLogWriteEntity map(final ProcessLog processLog) {
        final ProcessLogWriteEntity parent = ProcessLogWriteEntity.builder()
                .communicationLogs(new HashSet<>())
                .faultDescription(processLog.getFaultDescription())
                .processId(processLog.getProcessId())
                .request(processLog.getRequest())
                .response(processLog.getResponse())
                .status(processLog.getStatus())
                .deviceInformation(map(processLog.getDeviceInformation()))
                .createdAt(processLog.getCreatedAt())
                .modifiedAt(processLog.getModifiedAt())
                .build();

        final Set<CommunicationLogDetail> details =
                processLog.getCommunicationLogDetails().getCommunicationLogDetails();

        for (final CommunicationLogDetail d : details) {
            final CommunicationLogWriteEntity child = CommunicationLogWriteEntity.builder()
                    .processLog(parent)
                    .communicationLogId(d.getCommunicationLogId())
                    .serviceType(d.getServiceType())
                    .processType(d.getProcessType())
                    .createdAt(d.getCreatedAt())
                    .updatedAt(d.getUpdatedAt())
                    .request(d.getRequest())
                    .response(d.getResponse())
                    .deviceId(d.getDeviceId())
                    .departmentCode(d.getDepartmentCode())
                    .faultDescription(d.getFaultDescription())
                    .sourceService(d.getSourceService())
                    .targetService(d.getTargetService())
                    .build();
            parent.getCommunicationLogs().add(child);
        }

        return parent;
    }

    public static DeviceInformationEmbeddable map(final DeviceInformation deviceInformation) {
        return new DeviceInformationEmbeddable(deviceInformation.departmentCode(),
                deviceInformation.deviceId(), deviceInformation.deviceType(), deviceInformation.deviceUserType(),
                deviceInformation.userId(), deviceInformation.version());
    }

}
