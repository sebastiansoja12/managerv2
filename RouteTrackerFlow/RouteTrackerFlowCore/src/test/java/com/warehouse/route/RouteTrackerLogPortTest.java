package com.warehouse.route;

import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.route.domain.vo.SupplyInformation;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        final SupplyInformation supplyInformation = SupplyInformation.builder()
                .username("")
                .supplierId(1L)
                .created(LocalDateTime.now())
                .parcelId(1L)
                .depotCode("")
                .build();
        final RouteResponse expectedResponse = new RouteResponse(UUID.randomUUID());
        when(logPort.saveSupplyRoute(supplyInformation)).thenReturn(expectedResponse);
        // when
        final RouteResponse actualResponse = logPort.saveSupplyRoute(supplyInformation);
        // then
        assertEquals(actualResponse.getId(), expectedResponse.getId());
    }

    @Test
    void shouldSaveRoute() {
        // given
        final RouteRequest routeRequest = RouteRequest.builder().build();

        final RouteResponse expectedResponse = new RouteResponse(UUID.randomUUID());
        when(logPort.saveRoute(routeRequest)).thenReturn(expectedResponse);
        // when
        final RouteResponse actualResponse = logPort.saveRoute(routeRequest);
        // then
        assertEquals(expectedResponse.getId(), actualResponse.getId());
    }

    @Test
    void shouldSaveRoutesForMultipleParcels() {
        // given
        final RouteRequest routeRequest = RouteRequest.builder()
                .supplierId(1L)
                .parcelId(10001L)
                .id(ROUTE_ID)
                .depotId(1L)
                .userId(1L)
                .build();

        final RouteRequest routeRequest2 = RouteRequest.builder()
                .supplierId(1L)
                .parcelId(10002L)
                .id(ROUTE_ID_2)
                .depotId(1L)
                .userId(1L)
                .build();

        final List<RouteRequest> routeRequestList = Lists.list(routeRequest, routeRequest2);

        final RouteResponse expectedResponse1 = new RouteResponse(ROUTE_ID);
        final RouteResponse expectedResponse2 = new RouteResponse(ROUTE_ID_2);

        final List<RouteResponse> routeResponses = Lists.list(expectedResponse1, expectedResponse2);

        when(logPort.saveMultipleRoutes(routeRequestList)).thenReturn(routeResponses);
        // when
        final List<RouteResponse> actualResponse = logPort.saveMultipleRoutes(routeRequestList);
        // then
        assertEquals(routeResponses, actualResponse);
    }
}
