package com.warehouse.routetracker.infrastructure.adapter.secondary.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetail;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetails;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordDetailEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordDetailId;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordEntity;

@Mapper
public interface RouteLogToEntityMapper {

    @Mapping(target = "routeLogRecordDetails", ignore = true)
    RouteLogRecordEntity mapWithoutDetails(RouteLogRecord routeLogRecord);

    default RouteLogRecordEntity map(final RouteLogRecord routeLogRecord) {
        if (routeLogRecord == null) {
            return null;
        }

        final RouteLogRecordEntity entity = mapWithoutDetails(routeLogRecord);
        final List<RouteLogRecordDetailEntity> details = map(routeLogRecord.getRouteLogRecordDetails());
        details.forEach(detail -> detail.setRouteLogRecord(entity));
        entity.setRouteLogRecordDetails(details);
        return entity;
    }

	default List<RouteLogRecordDetailEntity> map(RouteLogRecordDetails routeLogRecordDetails) {
        return routeLogRecordDetails
                .getRouteLogRecordDetailSet()
                .stream()
                .map(this::mapToRouteLogRecordDetailEntity)
                .collect(Collectors.toList());
    }

	default RouteLogRecordDetailEntity mapToRouteLogRecordDetailEntity(RouteLogRecordDetail routeLogRecordDetail) {
        final RouteLogRecordDetailEntity entity = new RouteLogRecordDetailEntity();
        entity.setId(this.map(routeLogRecordDetail.getId()));
        entity.setEventId(routeLogRecordDetail.getEventId() == null ? null : routeLogRecordDetail.getEventId().toString());
        entity.setCreated(routeLogRecordDetail.getTimestamp());
        entity.setDepartmentId(routeLogRecordDetail.getDepartmentId());
        entity.setShipmentStatus(map(routeLogRecordDetail.getShipmentStatus()));
        entity.setRequest(routeLogRecordDetail.getRequest());
        entity.setDescription(routeLogRecordDetail.getDescription());
        entity.setUserId(routeLogRecordDetail.getUserId());
        entity.setVersion(routeLogRecordDetail.getVersion());
        entity.setSupplierId(routeLogRecordDetail.getSupplierId());
        entity.setProcessType(map(routeLogRecordDetail.getProcessType()));
        entity.setDeviceId(routeLogRecordDetail.getTerminalId() != null ? routeLogRecordDetail.getTerminalId().value() : null);
        return entity;
    }

	com.warehouse.routetracker.infrastructure.adapter.secondary.entity.enumeration.ProcessType map(
			ProcessType processType);

	com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.ShipmentStatus map(ShipmentStatus shipmentStatus);

    default RouteLogRecordDetailId map(final Long routeLogRecordDetailId) {
        return routeLogRecordDetailId != null
                ? new RouteLogRecordDetailId(routeLogRecordDetailId)
                : RouteLogRecordDetailId.generate();
    }

    default RouteLogRecordDetailEntity map(Long id, RouteLogRecordDetail routeLogRecordDetail) {
        final RouteLogRecordDetailEntity entity = new RouteLogRecordDetailEntity();
        entity.setId(this.map(id));
        entity.setEventId(routeLogRecordDetail.getEventId() == null ? null : routeLogRecordDetail.getEventId().toString());
        entity.setDepartmentId(routeLogRecordDetail.getDepartmentId());
        entity.setShipmentStatus(map(routeLogRecordDetail.getShipmentStatus()));
        entity.setRequest(routeLogRecordDetail.getRequest());
        entity.setDescription(routeLogRecordDetail.getDescription());
        entity.setUserId(routeLogRecordDetail.getUserId());
        entity.setVersion(routeLogRecordDetail.getVersion());
        entity.setSupplierId(routeLogRecordDetail.getSupplierId());
        entity.setProcessType(map(routeLogRecordDetail.getProcessType()));
        entity.setDeviceId(routeLogRecordDetail.getTerminalId() != null ? routeLogRecordDetail.getTerminalId().value() : null);
        return entity;
    }
}
