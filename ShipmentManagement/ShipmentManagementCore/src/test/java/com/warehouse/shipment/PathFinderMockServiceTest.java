package com.warehouse.shipment;

import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderMockService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathFinderMockServiceTest {

    private final PathFinderMockService mockService = new PathFinderMockService();

    @Test
    void shouldDetermineDeliveryDepot() {
        final Address address = new Address("Katowice", "00-000", "Katowicka 1");
        final VoronoiResponse voronoiResponse = mockService.determineDeliveryDepot(address);
        assertEquals(expectedToBe("KT3"), voronoiResponse.getValue());
    }

    @Test
    void shouldDetermineAnyDeliveryDepotWhenOneIsNotExpected() {
        final Address address = new Address("Los Angeles", "00-000", "Katowicka 1");
        final VoronoiResponse voronoiResponse = mockService.determineDeliveryDepot(address);
        assertEquals(expectedToBe("NCS"), voronoiResponse.getValue());
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
