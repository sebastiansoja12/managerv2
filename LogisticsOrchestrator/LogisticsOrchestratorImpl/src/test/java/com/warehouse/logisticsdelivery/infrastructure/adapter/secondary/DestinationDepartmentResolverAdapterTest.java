package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.geocoding.api.GeocodingCoordinatesDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesService;
import com.warehouse.logisticsdelivery.domain.vo.RecipientAddress;
import com.warehouse.organisationstructure.deliveryarea.api.DeliveryAreaApiService;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.organisationstructure.department.api.dto.CoordinatesDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDto;
import com.warehouse.voronoi.VoronoiResponseDto;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.dto.DepartmentCodeDto;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DestinationDepartmentResolverAdapterTest {

    private static final RecipientAddress ADDRESS = new RecipientAddress("Warszawa", "Długa 1", "00-001", "PL");

    private final GeocodingCoordinatesService geocodingCoordinatesService = mock(GeocodingCoordinatesService.class);
    private final DeliveryAreaApiService deliveryAreaApiService = mock(DeliveryAreaApiService.class);
    private final VoronoiService voronoiService = mock(VoronoiService.class);
    private final DepartmentApiService departmentApiService = mock(DepartmentApiService.class);

    private final DestinationDepartmentResolverAdapter adapter = new DestinationDepartmentResolverAdapter(
            geocodingCoordinatesService, deliveryAreaApiService, voronoiService, departmentApiService);

    @Test
    void shouldUseServingDeliveryArea() {
        when(geocodingCoordinatesService.findCoordinates(any())).thenReturn(new GeocodingCoordinatesDto(52.2, 21.0));
        when(deliveryAreaApiService.findServingDepartment(any())).thenReturn(Optional.of(new DepartmentId(5L)));

        assertThat(adapter.resolve(ADDRESS)).contains(new DepartmentId(5L));
        verifyNoInteractions(voronoiService);
    }

    @Test
    void shouldFallBackToNearestDepartmentWhenNoAreaCoversAddress() {
        when(geocodingCoordinatesService.findCoordinates(any())).thenReturn(new GeocodingCoordinatesDto(52.2, 21.0));
        when(deliveryAreaApiService.findServingDepartment(any())).thenReturn(Optional.empty());
        when(departmentApiService.getAllDepartments()).thenReturn(List.of(
                department(7L, "WA1", new CoordinatesDto(52.23, 21.01)),
                department(8L, "XX1", null)));
        when(voronoiService.findFastestRoute(any())).thenReturn(new VoronoiResponseDto(new DepartmentCodeDto("WA1"), "Warszawa"));
        when(departmentApiService.getDepartmentByCode(new DepartmentCode("WA1")))
                .thenReturn(department(7L, "WA1", new CoordinatesDto(52.23, 21.01)));

        assertThat(adapter.resolve(ADDRESS)).contains(new DepartmentId(7L));
    }

    @Test
    void shouldReturnEmptyWhenGeocodingFails() {
        when(geocodingCoordinatesService.findCoordinates(any())).thenThrow(new IllegalStateException("timeout"));

        assertThat(adapter.resolve(ADDRESS)).isEmpty();
        verifyNoInteractions(deliveryAreaApiService, voronoiService);
    }

    private DepartmentDto department(final Long departmentId, final String departmentCode,
                                     final CoordinatesDto coordinates) {
        return new DepartmentDto(departmentId, departmentCode, "Warszawa", "Długa 1", "00-001", "PL", coordinates);
    }
}
