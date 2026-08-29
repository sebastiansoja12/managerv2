package com.warehouse.routetracker.infrastructure.adapter.secondary.mapper;

import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetail;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetails;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.DepartmentId;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.OperatorId;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordDetailEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordDetailId;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordId;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.SupplierId;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.UserId;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper
public interface RouteLogToEntityMapper {

    default RouteLogRecordEntity map(final RouteLogRecord routeLogRecord) {
        if (routeLogRecord == null) {
            return null;
        }

        final List<RouteLogRecordDetailEntity> details = new ArrayList<>();
        final RouteLogRecordEntity entity = RouteLogRecordEntity.builder()
                .id(map(routeLogRecord.getId()))
                .shipmentId(routeLogRecord.getShipmentId())
                .returnCode(routeLogRecord.getReturnCode())
                .faultDescription(routeLogRecord.getFaultDescription())
                .routeLogRecordDetails(details)
                .build();
        details.addAll(map(routeLogRecord.getRouteLogRecordDetails(), entity));
        return entity;
    }

    default List<RouteLogRecordDetailEntity> map(final RouteLogRecordDetails routeLogRecordDetails,
                                                 final RouteLogRecordEntity routeLogRecordEntity) {
        return routeLogRecordDetails
                .getRouteLogRecordDetailSet()
                .stream()
                .map(detail -> map(detail, routeLogRecordEntity))
                .toList();
    }

    default RouteLogRecordDetailEntity map(final RouteLogRecordDetail detail,
                                           final RouteLogRecordEntity routeLogRecordEntity) {
        return RouteLogRecordDetailEntity.builder()
                .id(map(detail.getId()))
                .eventId(detail.getEventId() == null ? null : detail.getEventId().toString())
                .routeLogRecord(routeLogRecordEntity)
                .deviceId(detail.getTerminalId() == null ? null : detail.getTerminalId().value())
                .version(detail.getVersion())
                .created(detail.getTimestamp())
                .description(detail.getDescription())
                .processType(map(detail.getProcessType()))
                .request(detail.getRequest())
                .shipmentStatus(map(detail.getShipmentStatus()))
                .operatorId(detail.getOperatorId() == null ? null : new OperatorId(detail.getOperatorId().value()))
                .userId(detail.getUserId() == null ? null : new UserId(detail.getUserId().value()))
                .departmentId(detail.getDepartmentId() == null
                        ? null
                        : new DepartmentId(detail.getDepartmentId().value()))
                .supplierId(detail.getSupplierId() == null ? null : new SupplierId(detail.getSupplierId().value()))
                .build();
    }

    com.warehouse.routetracker.infrastructure.adapter.secondary.entity.enumeration.ProcessType map(
            ProcessType processType);

    com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.ShipmentStatus map(
            ShipmentStatus shipmentStatus);

    default RouteLogRecordDetailId map(final Long routeLogRecordDetailId) {
        return routeLogRecordDetailId != null
                ? new RouteLogRecordDetailId(routeLogRecordDetailId)
                : RouteLogRecordDetailId.generate();
    }

    default RouteLogRecordId map(final UUID routeLogRecordId) {
        return new RouteLogRecordId(routeLogRecordId.toString());
    }

}
