package com.warehouse.shipment;

import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
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
        final Address address = new Address("Katowice", "00-000", "Katowicka 1");
        // when
        final VoronoiResponse voronoiResponse = mockAdapter.determineDeliveryDepot(address);
        // then
        assertEquals(expectedToBe("KT3"), voronoiResponse.getValue());
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
