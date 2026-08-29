package com.warehouse.routetracker.infrastructure.adapter.secondary.mapper;

import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetail;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetails;
import com.warehouse.routetracker.domain.vo.TerminalId;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.SupplierId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordDetailEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordEntity;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper
public interface RouteLogToModelMapper {
    default RouteLogRecord map(RouteLogRecordEntity routeLogRecord) {
        return RouteLogRecord
                .builder()
                .id(UUID.fromString(routeLogRecord.getId().value()))
                .returnCode(routeLogRecord.getReturnCode())
                .shipmentId(routeLogRecord.getShipmentId())
                .faultDescription(routeLogRecord.getFaultDescription())
                .routeLogRecordDetails(new RouteLogRecordDetails(map(routeLogRecord.getRouteLogRecordDetails())))
                .build();
    }

    default Set<RouteLogRecordDetail> map(List<RouteLogRecordDetailEntity> routeLogRecordDetailEntities) {
        return routeLogRecordDetailEntities.stream()
                .map(this::mapToRouteLogRecordDetail)
                .collect(Collectors.toSet());
    }

    default RouteLogRecordDetail mapToRouteLogRecordDetail(RouteLogRecordDetailEntity routeLogRecordDetailEntity) {
        return RouteLogRecordDetail
                .builder()
                .id(routeLogRecordDetailEntity.getId().value())
                .eventId(routeLogRecordDetailEntity.getEventId() == null
                        ? null
                        : UUID.fromString(routeLogRecordDetailEntity.getEventId()))
                .shipmentStatus(map(routeLogRecordDetailEntity.getShipmentStatus()))
                .request(routeLogRecordDetailEntity.getRequest())
                .processType(map(routeLogRecordDetailEntity.getProcessType()))
                .description(routeLogRecordDetailEntity.getDescription())
                .timestamp(routeLogRecordDetailEntity.getCreated())
                .departmentId(routeLogRecordDetailEntity.getDepartmentId() == null
                        ? null
                        : new DepartmentId(routeLogRecordDetailEntity.getDepartmentId().value()))
                .operatorId(routeLogRecordDetailEntity.getOperatorId() == null
                        ? null
                        : new OperatorId(routeLogRecordDetailEntity.getOperatorId().value()))
                .supplierId(routeLogRecordDetailEntity.getSupplierId() == null
                        ? null
                        : new SupplierId(routeLogRecordDetailEntity.getSupplierId().value()))
                .userId(routeLogRecordDetailEntity.getUserId() == null
                        ? null
                        : new UserId(routeLogRecordDetailEntity.getUserId().value()))
                .version(routeLogRecordDetailEntity.getVersion())
                .terminalId(routeLogRecordDetailEntity.getDeviceId() != null ? new TerminalId(routeLogRecordDetailEntity.getDeviceId()) : null)
                .build();
    }

    ProcessType map(com.warehouse.routetracker.infrastructure.adapter.secondary.entity.enumeration.ProcessType processType);

    ShipmentStatus map(com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.ShipmentStatus shipmentStatus);
}
