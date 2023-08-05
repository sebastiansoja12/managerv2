package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import com.warehouse.dto.CoordinatesDto;
import com.warehouse.dto.DepotDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.warehouse.depot.domain.port.primary.DepotPort;
import com.warehouse.shipment.domain.model.Recipient;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderAdapter;
import com.warehouse.voronoi.VoronoiService;

@ExtendWith(MockitoExtension.class)
public class PathFinderAdapterTest {
    
    @Mock
    private VoronoiService voronoiService;
    
    @Mock
    private DepotPort depotPort;
    
    private PathFinderAdapter pathFinderAdapter;
    
    
    
    
    @BeforeEach
    void setup() {
        pathFinderAdapter = new PathFinderAdapter(voronoiService, depotPort);
    }
    
    @Test
    void shouldDetermineNewDeliveryDepot() {
        // given
        final Recipient recipient = Recipient.builder()
                .city("Krakow")
                .build();

        // mock list of depots
		final List<DepotDto> depots = List
				.of(new DepotDto("Krakow", "krakowska", "POL", "KR1", new CoordinatesDto(10.0, 10.0)));

        // mock the nearest depot to deliver
        when(voronoiService.findFastestRoute(depots, recipient.getCity())).thenReturn("Krakow");
        
        // when
        final String city = voronoiService.findFastestRoute(depots, recipient.getCity());
        
        // then
        assertEquals(city, expectedToBe(recipient.getCity()));
    }

    private String expectedToBe(String city) {
        return city;
    }

}
