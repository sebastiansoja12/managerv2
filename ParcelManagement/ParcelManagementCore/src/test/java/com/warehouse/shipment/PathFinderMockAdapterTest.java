package com.warehouse.shipment;

import com.warehouse.shipment.domain.model.Address;
import com.warehouse.shipment.domain.model.City;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderMockAdapter;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderMockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        final Address address = Address.builder()
                .city("Katowice")
                .postalCode("00-000")
                .street("Katowicka 1")
                .build();
        // when
        final City city = mockAdapter.determineDeliveryDepot(address);
        // then
        assertEquals(expectedToBe("KT3"), city.getValue());
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
