package com.warehouse.route;

import com.warehouse.route.configuration.RouteTrackerTestConfiguration;
import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.route.domain.vo.SupplyInformation;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteTrackerLogTest {

    @Mock
    private RouteTrackerLogPort routeTrackerLogPort;

    private final UUID ROUTE_ID = UUID.fromString("1d614a30-910f-486e-8c7b-e3043744088f");

    @Test
    void shouldSaveRoute() {
        // given
        final SupplyInformation supplyInformation = SupplyInformation.builder()
                .created(LocalDateTime.now())
                .depotCode("KT1")
                .parcelId(100001L)
                .supplierId(1L)
                .username("s-soja")
                .build();
        final RouteResponse response = new RouteResponse(ROUTE_ID);
        when(routeTrackerLogPort.saveSupplyRoute(supplyInformation)).thenReturn(response);
        // when
        final RouteResponse route = routeTrackerLogPort.saveSupplyRoute(supplyInformation);
        // then
        assertThat(route.getId()).isNotNull();
    }
}
