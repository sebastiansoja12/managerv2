package com.warehouse.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.delivery.domain.enumeration.DeliveryStatus;
import com.warehouse.delivery.domain.model.DeliveryRouteRequest;
import com.warehouse.delivery.domain.model.DeliveryRouteResponse;
import com.warehouse.delivery.infrastructure.adapter.secondary.RouteLogAdapter;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryMapper;
import com.warehouse.routetracker.infrastructure.api.RouteLogEventPublisher;

@ExtendWith(MockitoExtension.class)
public class RouteLogAdapterTest {
    @Mock
    private RouteLogEventPublisher routeLogEventPublisher;

    private final DeliveryMapper deliveryMapper = Mappers.getMapper(DeliveryMapper.class);

    private RouteLogAdapter routeLogAdapter;

    @BeforeEach
    void setup() {
        routeLogAdapter = new RouteLogAdapter(routeLogEventPublisher, deliveryMapper);
    }

    @Test
    void shouldSendShipmentRequestEvent() {
        // given
        final Set<DeliveryRouteRequest> deliveryRouteRequests = new HashSet<>();
        deliveryRouteRequests.add(request());

        doNothing()
                .when(routeLogEventPublisher)
                .send(any());
        // when
        final List<DeliveryRouteResponse> response = routeLogAdapter.deliver(deliveryRouteRequests);
        // then
        assertEquals(expectedToBe(1), response.size());
        verify(routeLogEventPublisher, times(1)).send(any());
    }

    private DeliveryRouteRequest request() {
        return DeliveryRouteRequest.builder()
                .id(UUID.fromString("2ea6ba6b-3f01-4d04-ad6d-952a186f48ac"))
                .token("token")
                .supplierCode("code")
                .depotCode("KT1")
                .deliveryStatus(DeliveryStatus.DELIVERY)
                .build();
    }

    private <T> T expectedToBe(T t) {
        return t;
    }

}
