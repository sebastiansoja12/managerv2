package com.warehouse.route;

import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.route.domain.model.*;
import com.warehouse.route.domain.port.secondary.RouteRepository;
import com.warehouse.route.infrastructure.adapter.secondary.RouteLogAdapter;
import com.warehouse.route.infrastructure.adapter.secondary.mapper.RouteMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteLogAdapterTest {

    @Mock
    private RouteMapper routeMapper;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private AuthenticationPort authenticationPort;

    @InjectMocks
    private RouteLogAdapter adapter;

    private final UUID ROUTE_ID = UUID.fromString("558a76b8-0b36-4677-aede-915674b9f6a9");


    @Test
    void shouldSaveRoute() {
        // given
        final RouteRequest routeRequest = RouteRequest.builder()
                .depotId(1L)
                .parcelId(1L)
                .id(ROUTE_ID)
                .supplierId(1L)
                .userId(1L)
                .build();
        final Route route = Route.builder()
                .created(LocalDateTime.now())
                .parcelId(1L)
                .id(ROUTE_ID)
                .build();
        final RouteResponse response = new RouteResponse(ROUTE_ID);
        when(routeMapper.mapToRoute(routeRequest)).thenReturn(route);
        when(routeRepository.save(route)).thenReturn(response);
        when(adapter.saveRoute(routeRequest)).thenReturn(response);
        // when
        final RouteResponse actualResponse = adapter.saveRoute(routeRequest);
        // then
        assertThat(actualResponse.getId()).isEqualTo(ROUTE_ID);
    }

    @Test
    void shouldFindParcelById() {
        // given
        final Long parcelId = 1L;
        final Routes route = Routes.builder()
                .id(ROUTE_ID)
                .parcel(Parcel.builder()
                        .id(1L)
                        .build())
                .build();
        final List<Routes> routeList = Lists.list(route);
        when(routeRepository.findByParcelId(parcelId)).thenReturn(routeList);
        when(adapter.findByParcelId(parcelId)).thenReturn(routeList);
        // when
        final List<Routes> actualRouteList = adapter.findByParcelId(parcelId);
        // then
        assertThat(actualRouteList.size()).isEqualTo(1L);
    }

    @Test
    void shouldFindParcelByUsername() {
        // given
        final String username = "test";
        final Routes route = Routes.builder()
                .id(ROUTE_ID)
                .user(User.builder()
                        .username("test")
                        .build())
                .build();
        final List<Routes> routeList = Lists.list(route);
        when(routeRepository.findByUsername(username)).thenReturn(routeList);
        when(adapter.findByUsername(username)).thenReturn(routeList);
        // when
        final List<Routes> actualRouteList = adapter.findByUsername(username);
        // then
        assertThat(actualRouteList.size()).isEqualTo(1L);
    }
}

