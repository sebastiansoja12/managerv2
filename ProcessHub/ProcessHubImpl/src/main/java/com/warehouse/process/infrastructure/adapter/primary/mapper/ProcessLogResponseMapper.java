package com.warehouse.process.infrastructure.adapter.primary.mapper;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

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

    public static ProcessLogResponseApi map(final ProcessLog processLog, final Function<UserId, String> usernames) {
        return new ProcessLogResponseApi(
                processLog.getProcessId().value().toString(),
                processLog.getRequest(),
                processLog.getResponse(),
                processLog.getCreatedAt(),
                processLog.getModifiedAt(),
                processLog.getStatus() != null ? processLog.getStatus().name() : null,
                processLog.getFaultDescription(),
                map(processLog.getDeviceInformation(), usernames),
                processLog.getCommunicationLogDetails()
                        .getCommunicationLogDetails()
                        .stream()
                        .sorted(Comparator.comparing(CommunicationLogDetail::getCreatedAt,
                                Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                        .map(detail -> map(detail, usernames))
                        .toList()
        );
    }

    public static List<ProcessLogResponseApi> map(final List<ProcessLog> processLogs, final Function<UserId, String> usernames) {
        return processLogs.stream().map(processLog -> map(processLog, usernames)).toList();
    }

    public static Page<ProcessLogResponseApi> map(final Page<ProcessLog> processLogs, final Function<UserId, String> usernames) {
        return processLogs.map(processLog -> map(processLog, usernames));
    }

    private static DeviceInformationResponseApi map(final DeviceInformation deviceInformation, final Function<UserId, String> usernames) {
        if (deviceInformation == null) {
            return null;
        }

        return new DeviceInformationResponseApi(
                value(deviceInformation.deviceId()),
                value(deviceInformation.departmentCode()),
                value(deviceInformation.userId()),
                username(deviceInformation.userId(), usernames),
                deviceInformation.deviceType() != null ? deviceInformation.deviceType().name() : null,
                deviceInformation.deviceUserType() != null ? deviceInformation.deviceUserType().name() : null,
                deviceInformation.version()
        );
    }

    private static CommunicationLogResponseApi map(final CommunicationLogDetail detail, final Function<UserId, String> usernames) {
        return new CommunicationLogResponseApi(
                detail.getCommunicationLogId() != null ? detail.getCommunicationLogId().value() : null,
                value(detail.getDeviceId()),
                detail.getProcessType() != null ? detail.getProcessType().name() : null,
                detail.getServiceType() != null ? detail.getServiceType().name() : null,
                value(detail.getCreatedBy()),
                value(detail.getUpdatedBy()),
                username(detail.getCreatedBy(), usernames),
                username(detail.getUpdatedBy(), usernames),
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

    private static String username(final UserId userId, final Function<UserId, String> usernames) {
        return userId != null ? usernames.apply(userId) : null;
    }

    private static Long value(final UserId userId) {
        return userId != null ? userId.value() : null;
    }
}
