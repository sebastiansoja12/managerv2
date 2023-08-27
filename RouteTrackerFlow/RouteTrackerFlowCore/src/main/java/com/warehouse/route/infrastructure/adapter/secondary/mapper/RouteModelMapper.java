package com.warehouse.route.infrastructure.adapter.secondary.mapper;

import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.Routes;
import com.warehouse.route.infrastructure.adapter.secondary.entity.RouteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RouteModelMapper {

    @Mapping(source = "parcel.id", target = "parcelId")
    @Mapping(source = "depot.id", target = "depotId")
    @Mapping(source = "user.id", target = "userId")
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
    Routes mapToRoutes(RouteEntity routeEntity);

    List<Routes> mapToRoutes(List<RouteEntity> routeEntityList);


    @Mapping(target = "parcel.id", source = "parcelId")
    @Mapping(target = "supplier.supplierCode", source = "supplierCode")
    @Mapping(target = "depot.id", source = "depotId")
    @Mapping(target = "user.id", source = "userId")
    RouteEntity map(Route route);

    @Mapping(target = "parcel.id", source = "parcelId")
    RouteEntity mapInitialize(Route route);


    @Mapping(source = "id", target = "id")
    RouteResponse mapToRouteResponse(RouteEntity routeEntity);

}
