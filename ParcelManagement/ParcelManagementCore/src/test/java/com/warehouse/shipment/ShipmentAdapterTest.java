package com.warehouse.shipment;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.shipment.domain.model.ShipmentResponse;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentAdapter;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentMapperImpl;

@ExtendWith(MockitoExtension.class)
public class ShipmentAdapterTest {


    @Mock
    private RouteLogEventPublisher routeLogEventPublisher;

    private ShipmentAdapter shipmentAdapter;

    private final ShipmentMapper shipmentMapper = new ShipmentMapperImpl();

    @BeforeEach
    void setup() {
        shipmentAdapter = new ShipmentAdapter(shipmentMapper, routeLogEventPublisher);
    }

    @Test
    void shouldSendShipmentRequestEvent() {
        // given
        final ShipmentResponse shipmentResponse = new ShipmentResponse("paymentUrl", 1L);
        // when
        shipmentAdapter.sendEvent(shipmentResponse);
        // then
        verify(routeLogEventPublisher, times(1)).send(any());
    }

}
