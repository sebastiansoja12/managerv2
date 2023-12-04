package com.warehouse.routetracker.infrastructure.adapter.secondary.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.domain.model.SupplyRoute;
import com.warehouse.routetracker.domain.vo.RouteResponse;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.*;

@Mapper
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
    @Mapping(target = "status", source = "parcel.status")
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
        if (Objects.isNull(routeLogRecord.getParcelStatus())) {
            return ParcelEntity.builder()
                    .id(routeLogRecord.getParcelId())
                    .build();
        }
        return ParcelEntity.builder()
                .id(routeLogRecord.getParcelId())
                .status(mapEntityStatus(routeLogRecord.getParcelStatus()))
                .build();
    }

    default com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status mapEntityStatus(ParcelStatus parcelStatus) {
        return switch (parcelStatus.name()) {
            case "REROUTE" -> com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status.REROUTE;
            case "SENT" -> com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status.SENT;
            case "DELIVERY" -> com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status.DELIVERY;
            case "RETURN" -> com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status.RETURN;
            case "REDIRECT" -> com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status.REDIRECT;
            default -> com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status.CREATED;
        };
    }

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

}
