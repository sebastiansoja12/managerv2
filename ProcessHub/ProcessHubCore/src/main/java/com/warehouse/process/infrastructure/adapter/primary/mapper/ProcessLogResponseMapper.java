package com.warehouse.process.infrastructure.adapter.primary.mapper;

import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.process.domain.model.CommunicationLogDetail;
import com.warehouse.process.domain.model.DeviceInformation;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.infrastructure.adapter.primary.api.CommunicationLogResponseApi;
import com.warehouse.process.infrastructure.adapter.primary.api.DeviceInformationResponseApi;
import com.warehouse.process.infrastructure.adapter.primary.api.ProcessLogResponseApi;

public abstract class ProcessLogResponseMapper {

    public static ProcessLogResponseApi map(final ProcessLog processLog) {
        return new ProcessLogResponseApi(
                processLog.getProcessId().value().toString(),
                processLog.getRequest(),
                processLog.getResponse(),
                processLog.getCreatedAt(),
                processLog.getModifiedAt(),
                processLog.getStatus() != null ? processLog.getStatus().name() : null,
                processLog.getFaultDescription(),
                map(processLog.getDeviceInformation()),
                processLog.getCommunicationLogDetails()
                        .getCommunicationLogDetails()
                        .stream()
                        .sorted(Comparator.comparing(CommunicationLogDetail::getCreatedAt,
                                Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                        .map(ProcessLogResponseMapper::map)
                        .toList()
        );
    }

    public static List<ProcessLogResponseApi> map(final List<ProcessLog> processLogs) {
        return processLogs.stream().map(ProcessLogResponseMapper::map).toList();
    }

    public static Page<ProcessLogResponseApi> map(final Page<ProcessLog> processLogs) {
        return processLogs.map(ProcessLogResponseMapper::map);
    }

    private static DeviceInformationResponseApi map(final DeviceInformation deviceInformation) {
        if (deviceInformation == null) {
            return null;
        }

        return new DeviceInformationResponseApi(
                value(deviceInformation.deviceId()),
                value(deviceInformation.departmentCode()),
                value(deviceInformation.userId()),
                deviceInformation.deviceType() != null ? deviceInformation.deviceType().name() : null,
                deviceInformation.deviceUserType() != null ? deviceInformation.deviceUserType().name() : null,
                deviceInformation.version()
        );
    }

    private static CommunicationLogResponseApi map(final CommunicationLogDetail detail) {
        return new CommunicationLogResponseApi(
                detail.getCommunicationLogId() != null ? detail.getCommunicationLogId().value() : null,
                value(detail.getDeviceId()),
                detail.getProcessType() != null ? detail.getProcessType().name() : null,
                detail.getServiceType() != null ? detail.getServiceType().name() : null,
                value(detail.getCreatedBy()),
                value(detail.getUpdatedBy()),
                value(detail.getDepartmentCode()),
                detail.getSourceService(),
                detail.getTargetService(),
                detail.getRequest(),
                detail.getResponse(),
                detail.getCreatedAt(),
                detail.getUpdatedAt(),
                detail.getFaultDescription()
        );
    }

    private static String value(final DeviceId deviceId) {
        return deviceId != null ? deviceId.value() : null;
    }

    private static String value(final DepartmentCode departmentCode) {
        return departmentCode != null ? departmentCode.value() : null;
    }

    private static Long value(final UserId userId) {
        return userId != null ? userId.value() : null;
    }
}
