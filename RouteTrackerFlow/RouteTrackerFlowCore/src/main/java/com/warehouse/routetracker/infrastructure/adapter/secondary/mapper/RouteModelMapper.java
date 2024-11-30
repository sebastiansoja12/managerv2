package com.warehouse.routetracker.infrastructure.adapter.secondary.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetail;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetails;
import com.warehouse.routetracker.domain.vo.RouteProcess;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordDetailEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RouteModelMapper {
    RouteLogRecord mapToRecord(RouteLogRecordEntity routeLogRecordToChange);

    default RouteLogRecordDetails mapToRouteLogRecordDetails(List<RouteLogRecordDetailEntity> value) {
        return RouteLogRecordDetails.builder().routeLogRecordDetailSet(mapToLogRecordDetails(value)).build();
    }

    Set<RouteLogRecordDetail> mapToLogRecordDetails(List<RouteLogRecordDetailEntity> value);


    @Mapping(target = "depotCode", source = "depot.depotCode")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "supplierCode", source = "supplier.supplierCode")
    RouteLogRecordDetail map(RouteLogRecordDetailEntity routeLogRecordDetailEntity);

    @Mapping(target = "processId", source = "id")
    @Mapping(target = "shipmentId.value", source = "parcelId")
    RouteProcess map(RouteLogRecordEntity entity);
}
