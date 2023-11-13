package com.warehouse.routetracker.infrastructure.adapter.secondary.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.routetracker.domain.enumeration.Status;
import com.warehouse.routetracker.domain.vo.Route;
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
    Route map(RouteEntity routeEntity);

    List<Route> map(List<RouteEntity> routeEntityList);

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

    default RouteEntity map(Route route) {
        return RouteEntity.builder()
                .created(LocalDateTime.now())
                .depot(mapDepotEntity(route))
                .parcel(mapParcelEntity(route))
                .supplier(mapSupplierEntity(route))
                .user(mapUserEntity(route))
                .build();
    }

    default RouteEntity map(SupplyRoute supplyRoute) {
        final Route route = supplyRoute.getRoute();
        final Status status = supplyRoute.getStatus();
        return RouteEntity.builder()
                .created(LocalDateTime.now())
                .depot(mapDepotEntity(route))
                .parcel(mapParcelEntity(route, status))
                .supplier(mapSupplierEntity(route))
                .user(mapUserEntity(route))
                .build();
    }

    default UserEntity mapUserEntity(Route route) {
        final String username = route.getUsername();
        if (StringUtils.isNotEmpty(username)) {
            return UserEntity.builder()
                    .username(username)
                    .build();
        }
        return null;
    }

    default ParcelEntity mapParcelEntity(Route route, Status status) {
        return ParcelEntity.builder()
                .id(route.getParcelId())
                .status(mapEntityStatus(status))
                .build();
    }

    default com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status mapEntityStatus(Status status) {
        return switch (status.name()) {
            case "REROUTE" -> com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status.REROUTE;
            case "SENT" -> com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status.SENT;
            case "DELIVERY" -> com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status.DELIVERY;
            case "RETURN" -> com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status.RETURN;
            case "REDIRECT" -> com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status.REDIRECT;
            default -> com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status.CREATED;
        };
    }

    default ParcelEntity mapParcelEntity(Route route) {
        return ParcelEntity.builder()
                .id(route.getParcelId())
                .build();
    }

	default DepotEntity mapDepotEntity(Route route) {
		final String depotCode = route.getDepotCode();
		if (StringUtils.isNotEmpty(depotCode)) {
			return DepotEntity.builder()
                    .depotCode(depotCode)
                    .build();
		}
		return null;
	}

    default SupplierEntity mapSupplierEntity(Route route) {
        final String supplierCode = route.getSupplierCode();
        if (StringUtils.isNotEmpty(supplierCode)) {
            return SupplierEntity.builder()
                    .supplierCode(supplierCode)
                    .build();
        }
        return null;
    }

}
