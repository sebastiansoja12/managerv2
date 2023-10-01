package com.warehouse.route;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.route.domain.vo.DeliveryInformation;

@ExtendWith(MockitoExtension.class)
public class RouteTrackerLogPortTest {

    @Mock
    private RouteTrackerLogPort logPort;

    private final static UUID ROUTE_ID = UUID.fromString("4cab76e2-5215-4625-b7e0-50e6fc65dc29");

    private final static UUID ROUTE_ID_2 = UUID.fromString("ef42becb-3074-462b-af86-62395ca47207");

    @Test
    void shouldInitializeRoute() {
        // given
        final Long parcelId = 1L;
        // when
        logPort.initializeRoute(parcelId);
        // then
        verify(logPort).initializeRoute(parcelId);
    }

    @Test
    void shouldSaveSupplyInformation() {
        // given
        final DeliveryInformation deliveryInformation = DeliveryInformation.builder()
                .supplierCode("abc")
                .parcelId(1L)
                .depotCode("")
                .build();
        final RouteResponse expectedResponse = new RouteResponse(UUID.randomUUID());
        //when(logPort.saveDelivery(Collections.singletonList(deliveryInformation))).thenReturn(expectedResponse);
        // when
        logPort.saveDelivery(Collections.singletonList(deliveryInformation));
        // then
        assertEquals(deliveryInformation.getParcelId(), expectedResponse.getId());
    }

    @Test
    void shouldSaveRoute() {
        // given
        final RouteRequest routeRequest = RouteRequest.builder().build();

        final RouteResponse expectedResponse = new RouteResponse(UUID.randomUUID());
        when(logPort.saveRoutes(List.of(routeRequest))).thenReturn(List.of(expectedResponse));
        // when
        final List<RouteResponse> actualResponse = logPort.saveRoutes(List.of(routeRequest));
        // then
        assertEquals(expectedResponse.getId(), actualResponse.get(0).getId());
    }

    @Test
    void shouldSaveRoutesForMultipleParcels() {
        // given
        final RouteRequest routeRequest = RouteRequest.builder()
                .supplierId(1L)
                .parcelId(10001L)
                .id(ROUTE_ID)
                .userId(1L)
                .build();

        final RouteRequest routeRequest2 = RouteRequest.builder()
                .supplierId(1L)
                .parcelId(10002L)
                .id(ROUTE_ID_2)
                .userId(1L)
                .build();

        final List<RouteRequest> routeRequestList = Lists.list(routeRequest, routeRequest2);

        final RouteResponse expectedResponse1 = new RouteResponse(ROUTE_ID);
        final RouteResponse expectedResponse2 = new RouteResponse(ROUTE_ID_2);

        final List<RouteResponse> routeResponses = Lists.list(expectedResponse1, expectedResponse2);

        //when(logPort.saveMultipleRoutes(routeRequestList)).thenReturn(routeResponses);
        // when
        //final List<RouteResponse> actualResponse = logPort.saveMultipleRoutes(routeRequestList);
        // then
       // assertEquals(routeResponses, actualResponse);
    }
}
