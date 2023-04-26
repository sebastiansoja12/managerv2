package com.warehouse.voronoi.mapper;

import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.infrastructure.adapter.primary.mapper.AddressRequestMapper;
import com.warehouse.voronoi.infrastructure.adapter.primary.mapper.AddressRequestMapperImpl;
import com.warehouse.depot.api.dto.CoordinatesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AddressRequestMapperTest {

    private AddressRequestMapper requestMapper;


    @BeforeEach
    void setup() {
        requestMapper = new AddressRequestMapperImpl();
    }


    @Test
    void shouldMap() {
        // given
        final CoordinatesDto coordinatesDto = CoordinatesDto.builder()
                .lat(50.01)
                .lon(50.02)
                .build();
        // when
        final Coordinates coordinates = requestMapper.map(coordinatesDto);
        // then
        assertAll(
                () -> assertThat(coordinates.getLat()).isEqualTo(coordinatesDto.getLat()),
                () -> assertThat(coordinates.getLon()).isEqualTo(coordinatesDto.getLon())
        );
    }
}
