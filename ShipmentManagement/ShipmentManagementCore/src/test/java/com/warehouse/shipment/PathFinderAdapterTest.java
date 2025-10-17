package com.warehouse.shipment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    
//    @Test
//    void shouldDetermineNewDeliveryDepot() {
//        // given
//        final DepartmentAddress address = new DepartmentAddress("Kraków", "Krakowska", "00-000");
//
//        doReturn(new VoronoiResponseDto("KR1", null))
//                .when(voronoiService)
//                .findFastestRoute(new VoronoiRequestDto(address.getCity(), "00-000"));
//        // when
//        final VoronoiResponse voronoiResponse = pathFinderAdapter.determineDeliveryDepartment(address);
//
//        // then
//        assertEquals(expectedToBe("KR1"), voronoiResponse.getValue());
//    }

    private <T> T expectedToBe(T value) {
        return value;
    }

}
