package com.warehouse.shipment.application.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;
import com.warehouse.shipment.application.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.application.port.secondary.UserServicePort;
import com.warehouse.shipment.domain.vo.RouteLogRecord;
import com.warehouse.shipment.domain.vo.RouteLogRecordDetail;
import com.warehouse.shipment.domain.vo.RouteLogRecordDetails;

public class RouteLogServiceImpl implements RouteLogService {

    private final RouteLogServicePort routeLogServicePort;

    private final DepartmentServicePort departmentServicePort;

    private final UserServicePort userServicePort;

    public RouteLogServiceImpl(final RouteLogServicePort routeLogServicePort,
                               final DepartmentServicePort departmentServicePort,
                               final UserServicePort userServicePort) {
        this.routeLogServicePort = routeLogServicePort;
        this.departmentServicePort = departmentServicePort;
        this.userServicePort = userServicePort;
    }

    @Override
    public Optional<RouteLogRecord> findByShipmentId(final ShipmentId shipmentId) {
        final Map<DepartmentId, DepartmentCode> departmentCodes = new HashMap<>();
        final Map<UserId, String> usernames = new HashMap<>();
        return Optional.ofNullable(this.routeLogServicePort.findByShipmentId(shipmentId))
                .map(routeLog -> resolveDetails(routeLog, departmentCodes, usernames));
    }

    private RouteLogRecord resolveDetails(
            final RouteLogRecord routeLog,
            final Map<DepartmentId, DepartmentCode> departmentCodes,
            final Map<UserId, String> usernames) {
        final Set<RouteLogRecordDetail> details = Optional.ofNullable(routeLog.routeLogRecordDetails())
                .map(RouteLogRecordDetails::routeLogRecordDetailSet)
                .orElseGet(Set::of)
                .stream()
                .map(detail -> resolveDepartmentCode(detail, departmentCodes))
                .map(detail -> resolveUsername(detail, usernames))
                .collect(Collectors.toSet());

        return new RouteLogRecord(routeLog.processId(), routeLog.shipmentId(), new RouteLogRecordDetails(details),
                routeLog.returnCode(), routeLog.faultDescription());
    }

    private RouteLogRecordDetail resolveDepartmentCode(
            final RouteLogRecordDetail detail,
            final Map<DepartmentId, DepartmentCode> departmentCodes) {
        return Optional.ofNullable(detail)
                .flatMap(routeLogDetail -> Optional.ofNullable(routeLogDetail.departmentId())
                        .map(departmentId -> departmentCodes.computeIfAbsent(
                                departmentId, this.departmentServicePort::getDepartmentCode))
                        .map(departmentCode -> withDepartmentCode(routeLogDetail, departmentCode)))
                .orElse(detail);
    }

    private RouteLogRecordDetail resolveUsername(
            final RouteLogRecordDetail detail,
            final Map<UserId, String> usernames) {
        return Optional.ofNullable(detail)
                .flatMap(routeLogDetail -> Optional.ofNullable(routeLogDetail.userId())
                        .map(userId -> usernames.computeIfAbsent(userId, this.userServicePort::getUsername))
                        .map(username -> withUsername(routeLogDetail, username)))
                .orElse(detail);
    }

    private RouteLogRecordDetail withDepartmentCode(
            final RouteLogRecordDetail detail,
            final DepartmentCode departmentCode) {
        return new RouteLogRecordDetail(detail.id(), detail.terminalId(), detail.version(), detail.userId(),
                detail.username(),
                detail.supplierCode(), detail.departmentId(), departmentCode.value(), detail.shipmentStatus(),
                detail.description(), detail.timestamp(), detail.processType(), detail.request());
    }

    private RouteLogRecordDetail withUsername(
            final RouteLogRecordDetail detail,
            final String username) {
        return new RouteLogRecordDetail(detail.id(), detail.terminalId(), detail.version(), detail.userId(), username,
                detail.supplierCode(), detail.departmentId(), detail.departmentCode(), detail.shipmentStatus(),
                detail.description(), detail.timestamp(), detail.processType(), detail.request());
    }
}
