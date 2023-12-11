package com.warehouse.routetracker.infrastructure.adapter.secondary.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.model.*;
import com.warehouse.routetracker.domain.vo.RouteProcess;
import com.warehouse.routetracker.domain.vo.RouteResponse;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RouteModelMapper {

    @Mapping(source = "parcel.id", target = "parcelId")
    @Mapping(source = "depot.depotCode", target = "depotCode")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "supplier.supplierCode", target = "supplierCode")
    RouteLogRecord map(RouteEntity routeEntity);

    List<RouteLogRecord> map(List<RouteEntity> routeEntityList);

    @Mapping(target = "parcel.sender.firstName", source = "parcel.firstName")
    @Mapping(target = "parcel.sender.lastName", source = "parcel.lastName")
    @Mapping(target = "parcel.sender.email", source = "parcel.senderEmail")
    @Mapping(target = "parcel.sender.telephoneNumber", source = "parcel.senderTelephone")
    @Mapping(target = "parcel.sender.city", source = "parcel.senderCity")
    @Mapping(target = "parcel.sender.postalCode", source = "parcel.senderPostalCode")
    @Mapping(target = "parcel.sender.street", source = "parcel.senderStreet")
    @Mapping(target = "parcel.recipient.firstName", source = "parcel.recipientFirstName")
    @Mapping(target = "parcel.recipient.lastName", source = "parcel.recipientLastName")
    @Mapping(target = "parcel.recipient.email", source = "parcel.recipientEmail")
    @Mapping(target = "parcel.recipient.telephoneNumber", source = "parcel.recipientTelephone")
    @Mapping(target = "parcel.recipient.city", source = "parcel.recipientCity")
    @Mapping(target = "parcel.recipient.postalCode", source = "parcel.recipientPostalCode")
    @Mapping(target = "parcel.recipient.street", source = "parcel.recipientStreet")
    @Mapping(target = "parcel.parcelSize", source = "parcel.parcelSize")
    @Mapping(target = "parcel.id", source = "parcel.id")
    @Mapping(target = "parcelStatus", source = "parcel.status")
    RouteInformation mapToRoutes(RouteEntity routeEntity);

    List<RouteInformation> mapToRoutes(List<RouteEntity> routeEntityList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "parcel.id", target = "parcelId")
    RouteResponse mapToRouteResponse(RouteEntity routeEntity);

    default RouteEntity map(RouteLogRecord routeLogRecord) {
        return RouteEntity.builder()
                .created(LocalDateTime.now())
                .depot(mapDepotEntity(routeLogRecord))
                .parcel(mapParcelEntity(routeLogRecord))
                .supplier(mapSupplierEntity(routeLogRecord))
                .user(mapUserEntity(routeLogRecord))
                .build();
    }

    default RouteEntity map(SupplyRoute supplyRoute) {
        final RouteLogRecord routeLogRecord = supplyRoute.getRouteLogRecord();
        return RouteEntity.builder()
                .created(LocalDateTime.now())
                .depot(mapDepotEntity(routeLogRecord))
                .parcel(mapParcelEntity(routeLogRecord))
                .supplier(mapSupplierEntity(routeLogRecord))
                .user(mapUserEntity(routeLogRecord))
                .build();
    }

    default UserEntity mapUserEntity(RouteLogRecord routeLogRecord) {
        final String username = routeLogRecord.getUsername();
        if (StringUtils.isNotEmpty(username)) {
            return UserEntity.builder()
                    .username(username)
                    .build();
        }
        return null;
    }

    default ParcelEntity mapParcelEntity(RouteLogRecord routeLogRecord) {
        return ParcelEntity.builder()
                .id(routeLogRecord.getParcelId())
                .status(mapEntityStatus(routeLogRecord.getParcelStatus()))
                .build();
    }

	com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status mapEntityStatus(
			ParcelStatus parcelStatus);

	default DepotEntity mapDepotEntity(RouteLogRecord routeLogRecord) {
		final String depotCode = routeLogRecord.getDepotCode();
		if (StringUtils.isNotEmpty(depotCode)) {
			return DepotEntity.builder()
                    .depotCode(depotCode)
                    .build();
		}
		return null;
	}

    default SupplierEntity mapSupplierEntity(RouteLogRecord routeLogRecord) {
        final String supplierCode = routeLogRecord.getSupplierCode();
        if (StringUtils.isNotEmpty(supplierCode)) {
            return SupplierEntity.builder()
                    .supplierCode(supplierCode)
                    .build();
        }
        return null;
    }

    RouteLogRecordToChange mapToRecord(RouteLogRecordEntity routeLogRecordToChange);

    default RouteLogRecordDetails mapToRouteLogRecordDetails(List<RouteLogRecordDetailEntity> value) {
        return RouteLogRecordDetails.builder().routeLogRecordDetailSet(mapToLogRecordDetails(value)).build();
    }

    Set<RouteLogRecordDetail> mapToLogRecordDetails(List<RouteLogRecordDetailEntity> value);

    @Mapping(target = "processId", source = "id")
    RouteProcess map(RouteLogRecordEntity entity);
}
