package com.warehouse.routetracker;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.warehouse.routetracker.domain.vo.RouteDeleteRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.routetracker.domain.model.Route;
import com.warehouse.routetracker.domain.vo.RouteRequest;
import com.warehouse.routetracker.domain.vo.RouteResponse;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.routetracker.domain.port.secondary.RouteRepository;
import com.warehouse.routetracker.domain.vo.DeliveryInformation;

@ExtendWith(MockitoExtension.class)
public class RouteTrackerLogTest {


    @Mock
    private RouteRepository repository;

    @InjectMocks
    private RouteTrackerLogPortImpl routeTrackerLogPort;

    private final UUID ROUTE_ID = UUID.fromString("1d614a30-910f-486e-8c7b-e3043744088f");

    private final UUID ROUTE_ID_2 = UUID.fromString("f0a45a51-6d0f-45ab-a839-41f161208c65");

    public static final Long PARCEL_ID = 100001L;

    public static final Long PARCEL_ID2 = 100002L;


    @Test
    void shouldSaveSupplyRoute() {
        // given
        final String depotCode = "KT1";
        final String supplierCode = "abc";
        final DeliveryInformation deliveryInformation = DeliveryInformation.builder()
                .depotCode(depotCode)
                .parcelId(PARCEL_ID)
                .supplierCode("supplierCode")
                .token("token")
                .build();
        final Route route = Route.builder()
                .supplierCode(supplierCode)
                .parcelId(PARCEL_ID)
                .depotCode(depotCode)
                .build();
        // when
        routeTrackerLogPort.saveDelivery(Collections.singletonList(deliveryInformation));
        // then
        //verify(repository, times(1)).saveSupplyRoute(route);
    }

    @Test
    void shouldNotSaveSupplyRouteWhenTokenDoesNotExist() {
        // given
        final String depotCode = "KT1";
        final String supplierCode = "abc";
        final DeliveryInformation deliveryInformation = DeliveryInformation.builder()
                .depotCode(depotCode)
                .parcelId(PARCEL_ID)
                .supplierCode("supplierCode")
                .build();
        final Route route = Route.builder()
                .supplierCode(supplierCode)
                .parcelId(PARCEL_ID)
                .depotCode(depotCode)
                .build();
        // when
        routeTrackerLogPort.saveDelivery(Collections.singletonList(deliveryInformation));
        // then
        //verify(repository, times(0)).saveSupplyRoute(route);
    }

    @Test
    void shouldSaveRoutes() {
        // given
        final RouteRequest routeRequest = RouteRequest.builder()
                .id(ROUTE_ID)
                .parcelId(100001L)
                .build();

        final RouteRequest routeRequest2 = RouteRequest.builder()
                .id(ROUTE_ID_2)
                .parcelId(PARCEL_ID2)
                .build();

        final List<RouteRequest> requests = Arrays.asList(routeRequest, routeRequest2);

        // create expected route response objects with uuid
        final RouteResponse response = new RouteResponse(ROUTE_ID);
        final RouteResponse response2 = new RouteResponse(ROUTE_ID_2);

        // model route objects sent to db
        final Route route = Route.builder()
                .parcelId(PARCEL_ID)
                .build();

        final Route route2 = Route.builder()
                .parcelId(PARCEL_ID2)
                .build();

        doReturn(response)
                .when(repository)
                .save(route);

        doReturn(response2)
                .when(repository)
                .save(route2);

        // when
        final List<RouteResponse> responses = routeTrackerLogPort.saveRoutes(requests);

        // then
        Assertions.assertEquals(expectedToBe(response), responses.get(0));
        Assertions.assertEquals(expectedToBe(response2), responses.get(1));
    }

    @Test
    void shouldDeleteRoute() {
        // given
        final String username = "s-soja";
        final RouteDeleteRequest deleteRequest = RouteDeleteRequest.builder()
                .id(ROUTE_ID)
                .parcelId(PARCEL_ID)
                .username(username)
                .build();
        // when
        routeTrackerLogPort.deleteRoute(deleteRequest);
        // then
        verify(repository, times(1)).deleteRoute(deleteRequest);
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
