package com.warehouse.organisationstructure.department.infrastructure.adapter.secondary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.geocoding.api.GeocodingAddressDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesService;
import com.warehouse.organisationstructure.department.domain.vo.Address;
import com.warehouse.organisationstructure.department.domain.vo.Coordinates;

class DepartmentCoordinatesServiceAdapterTest {

    private final GeocodingCoordinatesService geocodingCoordinatesService = mock(GeocodingCoordinatesService.class);

    private final DepartmentCoordinatesServiceAdapter adapter =
            new DepartmentCoordinatesServiceAdapter(geocodingCoordinatesService);

    @Test
    void shouldGeocodeDepartmentAddressWithCountryCode() {
        when(geocodingCoordinatesService.findCoordinates(
                new GeocodingAddressDto("Katowice", "Mariacka 1", "40-001", "PL")))
                .thenReturn(new GeocodingCoordinatesDto(50.26, 19.02));

        final Coordinates coordinates = adapter.getCoordinates(
                new Address("Katowice", "Mariacka 1", "40-001", CountryCode.PL));

        assertThat(coordinates).isEqualTo(new Coordinates(50.26, 19.02));
    }
}
