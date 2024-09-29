package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.City;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderAdapter;
import com.warehouse.voronoi.VoronoiService;

@ExtendWith(MockitoExtension.class)
public class PathFinderAdapterTest {
    
    @Mock
    private VoronoiService voronoiService;
    
    private PathFinderAdapter pathFinderAdapter;
    
    
    @BeforeEach
    void setup() {
        pathFinderAdapter = new PathFinderAdapter(voronoiService);
    }
    
    @Test
    void shouldDetermineNewDeliveryDepot() {
        // given
        final Address address = new Address("Kraków", "Krakowska", "00-000");

        doReturn("KR1")
                .when(voronoiService)
                .findFastestRoute(address.getCity());
        // when
        final City city = pathFinderAdapter.determineDeliveryDepot(address);
        
        // then
        assertEquals(expectedToBe("KR1"), city.getValue());
    }

    private <T> T expectedToBe(T value) {
        return value;
    }

}
