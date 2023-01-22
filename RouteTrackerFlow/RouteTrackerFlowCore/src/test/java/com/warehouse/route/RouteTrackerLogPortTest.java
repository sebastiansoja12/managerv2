package com.warehouse.route;

import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.route.domain.vo.SupplyInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteTrackerLogPortTest {

    @Mock
    private RouteTrackerLogPort logPort;

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
}
