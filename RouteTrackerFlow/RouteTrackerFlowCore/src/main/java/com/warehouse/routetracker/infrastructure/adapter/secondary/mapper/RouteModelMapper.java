package com.warehouse.routetracker.infrastructure.adapter.secondary.mapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.identificator.ShipmentId;
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


    @Mapping(target = "departmentCode", source = "department.departmentCode")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "supplierCode", source = "supplier.supplierCode")
    @Mapping(target = "terminalId.value", source = "deviceId")
    RouteLogRecordDetail map(RouteLogRecordDetailEntity routeLogRecordDetailEntity);

    @Mapping(target = "processId", source = "id")
    @Mapping(target = "shipmentId.value", source = "parcelId")
    default RouteProcess map(RouteLogRecordEntity entity) {
        return new RouteProcess(new ShipmentId(entity.getParcelId()), UUID.fromString(entity.getId()));
    }
}
