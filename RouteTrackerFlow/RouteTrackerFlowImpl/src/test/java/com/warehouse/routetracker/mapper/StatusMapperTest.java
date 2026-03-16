package com.warehouse.routetracker.mapper;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.StatusMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mapstruct.factory.Mappers.getMapper;

public class StatusMapperTest {
    
    private final StatusMapper mapper = getMapper(StatusMapper.class);
    
    @Test
    void shouldMap() {
        // given
        final ParcelStatus parcelStatus = ParcelStatus.RETURN;
        // when
		final com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status secondaryStatus = mapper
				.map(parcelStatus);
        // then
        assertEquals(parcelStatus.name(), secondaryStatus.name());
    }
}
