package com.warehouse.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.warehouse.delivery.domain.port.secondary.ParcelStatusControlChangeServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.delivery.domain.enumeration.DeliveryStatus;
import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.port.primary.DeliveryPortImpl;
import com.warehouse.delivery.domain.port.secondary.RouteLogServicePort;
import com.warehouse.delivery.domain.service.DeliveryService;

@ExtendWith(MockitoExtension.class)
public class DeliveryPortImplTest {


    @Mock
    private DeliveryService deliveryService;

    @Mock
    private RouteLogServicePort logServicePort;

    @Mock
    private ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort;

    private DeliveryPortImpl deliveryPort;

    private static final String DELIVERY_ID = "053e792f-6201-4365-a87a-f16e7f34b978";

    @BeforeEach
    void setup() {
        deliveryPort = new DeliveryPortImpl(deliveryService, logServicePort, parcelStatusControlChangeServicePort);
    }

    @Test
    void shouldDeliver() {
        // given
        final Set<DeliveryRequest> deliveryRequestSet = Set.of(createDeliveryRequest());
        final List<Delivery> delivery = Collections.singletonList(createDelivery());

        final Delivery updatedDelivery = createDelivery();
        updatedDelivery.setId(UUID.fromString(DELIVERY_ID));
        updatedDelivery.setParcelId(1L);

        doReturn(Collections.singletonList(updatedDelivery))
                .when(deliveryService)
                .save(deliveryRequestSet);

        // when
        final List<DeliveryResponse> deliveries = deliveryPort.deliver(deliveryRequestSet.stream().toList());
        // then
        assertThat(deliveries.size()).isEqualTo(1);
        final UUID id = deliveries.stream().map(DeliveryResponse::getId).findAny().orElse(null);
        assertEquals(expectedToBe(UUID.fromString(DELIVERY_ID)), id);
    }

    private Delivery createDelivery() {
        return Delivery.builder()
                .deliveryStatus(DeliveryStatus.DELIVERY)
                .depotCode("KT1")
                .supplierCode("abc_def")
                .parcelId(1L)
                .build();
    }

    private DeliveryRequest createDeliveryRequest() {
        return DeliveryRequest.builder()
                .depotCode("KT1")
                .supplierCode("abc_def")
                .parcelId(1L)
                .build();
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
