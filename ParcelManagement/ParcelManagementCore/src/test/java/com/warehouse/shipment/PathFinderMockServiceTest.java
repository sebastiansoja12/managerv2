package com.warehouse.shipment;

import com.warehouse.shipment.domain.model.Address;
import com.warehouse.shipment.domain.model.City;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderMockService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathFinderMockServiceTest {

    private final PathFinderMockService mockService = new PathFinderMockService();

    @Test
    void shouldDetermineDeliveryDepot() {
        // given
        final Address address = Address.builder()
                .city("Katowice")
                .postalCode("00-000")
                .street("Katowicka 1")
                .build();
        // when
        final City city = mockService.determineDeliveryDepot(address);
        // then
        assertEquals(expectedToBe("KT3"), city.getValue());
    }

    @Test
    void shouldDetermineAnyDeliveryDepotWhenOneIsNotExpected() {
        // given
        final Address address = Address.builder()
                .city("Los Angeles")
                .postalCode("00-000")
                .street("Katowicka 1")
                .build();
        // when
        final City city = mockService.determineDeliveryDepot(address);
        // then
        assertEquals(expectedToBe("NCS"), city.getValue());
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
