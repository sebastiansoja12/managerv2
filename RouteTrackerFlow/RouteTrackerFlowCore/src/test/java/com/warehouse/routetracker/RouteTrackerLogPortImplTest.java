package com.warehouse.routetracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.routetracker.domain.enumeration.Status;
import com.warehouse.routetracker.domain.model.Parcel;
import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.routetracker.domain.port.secondary.RouteRepository;
import com.warehouse.routetracker.domain.vo.*;

@ExtendWith(MockitoExtension.class)
public class RouteTrackerLogPortImplTest {


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
                .deliveryStatus(Status.DELIVERY)
                .parcelId(PARCEL_ID)
                .supplierCode(supplierCode)
                .token("token")
                .build();

        final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                .supplierCode(supplierCode)
                .parcelId(PARCEL_ID)
                .depotCode(depotCode)
                .status(Status.DELIVERY)
                .build();

        // when
        routeTrackerLogPort.saveDelivery(Collections.singletonList(deliveryInformation));
        // then
        verify(repository, times(1)).save(routeLogRecord);
    }

    @Test
    void shouldNotSaveSupplyRouteWhenTokenDoesNotExist() {
        // given
        final DeliveryInformation deliveryInformation = DeliveryInformation.builder().build();
        final RouteLogRecord routeLogRecord = RouteLogRecord.builder().build();
        // when
        routeTrackerLogPort.saveDelivery(Collections.singletonList(deliveryInformation));
        // then
        verify(repository, times(0)).save(routeLogRecord);
    }

    @Test
    void shouldSaveRoutes() {
        // given
        final RouteRequest routeRequest = RouteRequest.builder()
                .parcelId(100001L)
                .build();

        final RouteRequest routeRequest2 = RouteRequest.builder()
                .parcelId(PARCEL_ID2)
                .build();

        final List<RouteRequest> requests = Arrays.asList(routeRequest, routeRequest2);

        // create expected route response objects with uuid
        final RouteResponse response = RouteResponse.builder()
                .id(ROUTE_ID)
                .build();
        final RouteResponse response2 = RouteResponse.builder()
                .id(ROUTE_ID_2)
                .build();
        // model route objects sent to liquibase
        final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                .parcelId(PARCEL_ID)
                .build();

        final RouteLogRecord routeLogRecord2 = RouteLogRecord.builder()
                .parcelId(PARCEL_ID2)
                .build();

        doReturn(response)
                .when(repository)
                .save(routeLogRecord);

        doReturn(response2)
                .when(repository)
                .save(routeLogRecord2);

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
                .id("d8d53e7d-9175-4b5b-bf0d-bc209549c3a9")
                .build();
        // when
        routeTrackerLogPort.deleteRoute(deleteRequest);
        // then
        verify(repository, times(1)).deleteRoute(deleteRequest);
    }

    @Test
    void shouldFindByUsername() {
        // given
        final String username = "s-soja";
        final List<RouteInformation> expectedRouteInformations = Collections.singletonList(
                RouteInformation.builder()
                        .status(Status.RETURN)
                        .build()
        );
        doReturn(expectedRouteInformations)
                .when(repository)
                .findByUsername(username);
        // when
        final List<RouteInformation> routeInformations = routeTrackerLogPort.findRoutesByUsername(username);
        // then
        routeInformations.forEach(
                routeInformation -> assertEquals(expectedToBe(routeInformation.getStatus()), Status.RETURN)
        );

    }

    @Test
    void shouldFindByParcelId() {
        // given
        final Long parcelId = 1L;
        final List<RouteInformation> expectedRouteInformations = Collections.singletonList(
                RouteInformation.builder()
                        .parcel(Parcel.builder().id(parcelId).build())
                        .status(Status.RETURN)
                        .build()
        );
        doReturn(expectedRouteInformations)
                .when(repository)
                .findByParcelId(parcelId);
        // when
        final List<RouteInformation> routeInformations = routeTrackerLogPort.getRouteListByParcelId(parcelId);
        // then
        routeInformations.forEach(
                routeInformation -> assertEquals(expectedToBe(routeInformation.getStatus()), Status.RETURN)
        );

    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
