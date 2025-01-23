package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderAdapter;
import com.warehouse.voronoi.VoronoiRequestDto;
import com.warehouse.voronoi.VoronoiResponseDto;
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

        doReturn(new VoronoiResponseDto("KR1", null))
                .when(voronoiService)
                .findFastestRoute(new VoronoiRequestDto(address.getCity(), "00-000"));
        // when
        final VoronoiResponse voronoiResponse = pathFinderAdapter.determineDeliveryDepot(address);
        
        // then
        assertEquals(expectedToBe("KR1"), voronoiResponse.getValue());
    }

    private <T> T expectedToBe(T value) {
        return value;
    }

}
