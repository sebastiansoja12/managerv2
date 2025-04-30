package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderMockAdapter;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderMockService;

@ExtendWith(MockitoExtension.class)
public class PathFinderMockAdapterTest {

    private PathFinderMockService mockService;
    private PathFinderMockAdapter mockAdapter;

    @BeforeEach
    void setup() {
        mockService = new PathFinderMockService();
        mockAdapter = new PathFinderMockAdapter(mockService);
    }

    @Test
    void shouldDetermineDeliveryDepot() {
        // given
        final Address address = new Address("Katowice", "00-000", "Katowicka 1");
        final Shipment shipment = new Shipment();
        // when
        mockAdapter.determineDeliveryDepot(shipment, address);
        // then
        assertEquals(expectedToBe("KT3"), shipment.getDestination());
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
