package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderMockService;

public class PathFinderMockServiceTest {

    private final PathFinderMockService mockService = new PathFinderMockService();

    @Test
    void shouldDetermineDeliveryDepot() {
        final Address address = new Address("Katowice", "00-000", "Katowicka 1");
        final VoronoiResponse voronoiResponse = mockService.determineDeliveryDepot(address);
        assertEquals("KT3", voronoiResponse.getDepartmentCodeResult().toString());
    }

    @Test
    void shouldDetermineAnyDeliveryDepotWhenOneIsNotExpected() {
        final Address address = new Address("Los Angeles", "00-000", "Katowicka 1");
        final VoronoiResponse voronoiResponse = mockService.determineDeliveryDepot(address);
        assertEquals("NCS", voronoiResponse.getDepartmentCodeResult().toString());
    }

}
