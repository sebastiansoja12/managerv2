package com.warehouse.routetracker.infrastructure.adapter.secondary.mapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetail;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetails;
import com.warehouse.routetracker.domain.model.RouteLogRecordToChange;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordDetailEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordEntity;

@Mapper
public interface RouteLogToModelMapper {
    default RouteLogRecordToChange map(RouteLogRecordEntity routeLogRecord) {
        return RouteLogRecordToChange
                .builder()
                .id(UUID.fromString(routeLogRecord.getId()))
                .returnCode(routeLogRecord.getReturnCode())
                .parcelId(routeLogRecord.getParcelId())
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
                .id(routeLogRecordDetailEntity.getId())
                .parcelStatus(map(routeLogRecordDetailEntity.getParcelStatus()))
                .request(routeLogRecordDetailEntity.getRequest())
                .processType(map(routeLogRecordDetailEntity.getProcessType()))
                .description(routeLogRecordDetailEntity.getDescription())
                .timestamp(routeLogRecordDetailEntity.getCreated())
                .depotCode(routeLogRecordDetailEntity.getDepot() != null ?
                        routeLogRecordDetailEntity.getDepot().getDepotCode() : null)
                .supplierCode(routeLogRecordDetailEntity.getSupplier() != null ? routeLogRecordDetailEntity
                        .getSupplier().getSupplierCode() : null)
                .username(routeLogRecordDetailEntity.getUser() != null ?
                        routeLogRecordDetailEntity.getUser().getUsername() : null)
                .version(routeLogRecordDetailEntity.getVersion())
                .zebraId(routeLogRecordDetailEntity.getZebraId())
                .build();
    }

    ProcessType map(com.warehouse.routetracker.infrastructure.adapter.secondary.entity.enumeration.ProcessType processType);

    ParcelStatus map(com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.ParcelStatus parcelStatus);
}
