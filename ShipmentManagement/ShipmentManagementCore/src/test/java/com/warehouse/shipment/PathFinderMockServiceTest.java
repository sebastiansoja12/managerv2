package com.warehouse.shipment;

import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.City;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderMockService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathFinderMockServiceTest {

    private final PathFinderMockService mockService = new PathFinderMockService();

    @Test
    void shouldDetermineDeliveryDepot() {
        // given
        final Address address = new Address("Katowice", "00-000", "Katowicka 1");
        // when
        final City city = mockService.determineDeliveryDepot(address);
        // then
        assertEquals(expectedToBe("KT3"), city.getValue());
    }

    @Test
    void shouldDetermineAnyDeliveryDepotWhenOneIsNotExpected() {
        // given
        final Address address = new Address("Los Angeles", "00-000", "Katowicka 1");
        // when
        final City city = mockService.determineDeliveryDepot(address);
        // then
        assertEquals(expectedToBe("NCS"), city.getValue());
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
