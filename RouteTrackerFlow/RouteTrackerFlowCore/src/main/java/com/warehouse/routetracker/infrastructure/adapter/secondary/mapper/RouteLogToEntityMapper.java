package com.warehouse.routetracker.infrastructure.adapter.secondary.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetail;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetails;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.*;

@Mapper
public interface RouteLogToEntityMapper {

    @Mapping(target = "routeLogRecordDetails", source = "routeLogRecordDetails")
    RouteLogRecordEntity map(RouteLogRecord routeLogRecord);

	default List<RouteLogRecordDetailEntity> map(RouteLogRecordDetails routeLogRecordDetails) {
        return routeLogRecordDetails
                .getRouteLogRecordDetailSet()
                .stream()
                .map(this::mapToRouteLogRecordDetailEntity)
                .collect(Collectors.toList());
    }

	default RouteLogRecordDetailEntity mapToRouteLogRecordDetailEntity(RouteLogRecordDetail routeLogRecordDetail) {
        final RouteLogRecordDetailEntity entity = new RouteLogRecordDetailEntity();
        entity.setId(routeLogRecordDetail.getId());
        entity.setCreated(routeLogRecordDetail.getTimestamp());
        entity.setDepot(mapDepotEntity(routeLogRecordDetail));
        entity.setParcelStatus(map(routeLogRecordDetail.getParcelStatus()));
        entity.setRequest(routeLogRecordDetail.getRequest());
        entity.setDescription(routeLogRecordDetail.getDescription());
        entity.setUser(mapUserEntity(routeLogRecordDetail));
        entity.setVersion(routeLogRecordDetail.getVersion());
        entity.setSupplier(mapSupplierEntity(routeLogRecordDetail));
        entity.setProcessType(map(routeLogRecordDetail.getProcessType()));
        entity.setZebraId(routeLogRecordDetail.getTerminalId().getValue().longValue());
        return entity;
    }

    default UserEntity mapUserEntity(RouteLogRecordDetail routeLogRecord) {
        final String username = routeLogRecord.getUsername();
        if (StringUtils.isNotEmpty(username)) {
            return UserEntity.builder()
                    .username(username)
                    .build();
        }
        return null;
    }

    default DepotEntity mapDepotEntity(RouteLogRecordDetail routeLogRecord) {
        final String depotCode = routeLogRecord.getDepotCode();
        if (StringUtils.isNotEmpty(depotCode)) {
            return DepotEntity.builder()
                    .depotCode(depotCode)
                    .build();
        }
        return null;
    }

    default SupplierEntity mapSupplierEntity(RouteLogRecordDetail routeLogRecord) {
        final String supplierCode = routeLogRecord.getSupplierCode();
        if (StringUtils.isNotEmpty(supplierCode)) {
            return SupplierEntity.builder()
                    .supplierCode(supplierCode)
                    .build();
        }
        return null;
    }

	com.warehouse.routetracker.infrastructure.adapter.secondary.entity.enumeration.ProcessType map(
			ProcessType processType);

	com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.ParcelStatus map(ParcelStatus parcelStatus);

    default RouteLogRecordDetailEntity map(Long id, RouteLogRecordDetail routeLogRecordDetail) {
        final RouteLogRecordDetailEntity entity = new RouteLogRecordDetailEntity();
        entity.setId(id);
        entity.setDepot(mapDepotEntity(routeLogRecordDetail));
        entity.setParcelStatus(map(routeLogRecordDetail.getParcelStatus()));
        entity.setRequest(routeLogRecordDetail.getRequest());
        entity.setDescription(routeLogRecordDetail.getDescription());
        entity.setUser(mapUserEntity(routeLogRecordDetail));
        entity.setVersion(routeLogRecordDetail.getVersion());
        entity.setSupplier(mapSupplierEntity(routeLogRecordDetail));
        entity.setProcessType(map(routeLogRecordDetail.getProcessType()));
        entity.setZebraId(routeLogRecordDetail.getTerminalId().getValue().longValue());
        return entity;
    }
}
