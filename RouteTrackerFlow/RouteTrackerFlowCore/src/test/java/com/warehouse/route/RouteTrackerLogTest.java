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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteTrackerLogTest {

    @Mock
    private RouteTrackerLogPort routeTrackerLogPort;

    private final UUID ROUTE_ID = UUID.fromString("1d614a30-910f-486e-8c7b-e3043744088f");

    private final UUID ROUTE_ID_2 = UUID.fromString("f0a45a51-6d0f-45ab-a839-41f161208c65");


    @Test
    void shouldSaveRoute() {
        // given
        final SupplyInformation supplyInformation = SupplyInformation.builder()
                .created(LocalDateTime.now())
                .depotCode("KT1")
                .parcelId(100001L)
               // .supplierId(1L)
                .username("s-soja")
                .build();
        final RouteResponse response = new RouteResponse(ROUTE_ID);
        when(routeTrackerLogPort.saveSupplyRoute(Collections.singletonList(supplyInformation))).thenReturn(response);
        // when
        final RouteResponse route = routeTrackerLogPort.saveSupplyRoute(Collections.singletonList(supplyInformation));
        // then
        assertThat(route.getId()).isNotNull();
    }

    @Test
    void shouldSaveMultipleRoutes() {
        // given
        final RouteRequest routeRequest = RouteRequest.builder()
                .depotId(1L)
                .id(ROUTE_ID)
                .parcelId(100001L)
                .supplierId(1L)
                .build();

        final RouteRequest routeRequest2 = RouteRequest.builder()
                .depotId(1L)
                .id(ROUTE_ID_2)
                .parcelId(100002L)
                .supplierId(1L)
                .build();

        final RouteResponse response = new RouteResponse(ROUTE_ID);
        final RouteResponse response2 = new RouteResponse(ROUTE_ID_2);


        // when
    }
}
