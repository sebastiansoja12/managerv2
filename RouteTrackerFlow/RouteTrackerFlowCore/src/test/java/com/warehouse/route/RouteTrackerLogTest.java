package com.warehouse.route;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.route.domain.port.secondary.RouteLogService;
import com.warehouse.route.domain.vo.DeliveryInformation;

@ExtendWith(MockitoExtension.class)
public class RouteTrackerLogTest {


    @Mock
    private RouteLogService routeLogService;

    private RouteTrackerLogPortImpl routeTrackerLogPort;

    private final UUID ROUTE_ID = UUID.fromString("1d614a30-910f-486e-8c7b-e3043744088f");

    private final UUID ROUTE_ID_2 = UUID.fromString("f0a45a51-6d0f-45ab-a839-41f161208c65");

    public static final Long PARCEL_ID = 100001L;

    public static final long PARCEL_ID2 = 100002L;


    @BeforeEach
    void setup() {
        routeTrackerLogPort = new RouteTrackerLogPortImpl(routeLogService);
    }

    @Test
    void shouldSaveSupplyRoute() {
        // given
        final String depotCode = "KT1";
        final DeliveryInformation deliveryInformation = DeliveryInformation.builder()
                .depotCode(depotCode)
                .parcelId(PARCEL_ID)
                .supplierCode("supplierCode")
                .token("token")
                .build();
        final Route route = Route.builder()
                .supplierId(1L)
                .parcelId(PARCEL_ID)
                .depotCode(depotCode)
                .build();
        // when
        routeTrackerLogPort.saveDelivery(Collections.singletonList(deliveryInformation));
        // then
        verify(routeLogService, times(1)).saveSupplyRoute(route);
    }

    @Test
    void shouldNotSaveSupplyRouteWhenTokenDoesNotExist() {
        // given
        final String depotCode = "KT1";
        final DeliveryInformation deliveryInformation = DeliveryInformation.builder()
                .depotCode(depotCode)
                .parcelId(PARCEL_ID)
                .supplierCode("supplierCode")
                .build();
        final Route route = Route.builder()
                .supplierId(1L)
                .parcelId(PARCEL_ID)
                .depotCode(depotCode)
                .build();
        // when
        routeTrackerLogPort.saveDelivery(Collections.singletonList(deliveryInformation));
        // then
        verify(routeLogService, times(0)).saveSupplyRoute(route);
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
                .when(routeLogService)
                .saveRoute(route);

        doReturn(response2)
                .when(routeLogService)
                .saveRoute(route2);

        // when
        final List<RouteResponse> responses = routeTrackerLogPort.saveRoutes(requests);

        // then
        Assertions.assertEquals(expectedToBe(response), responses.get(0));
        Assertions.assertEquals(expectedToBe(response2), responses.get(1));
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
