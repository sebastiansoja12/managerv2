package com.warehouse.routetracker.mapper;

import com.warehouse.routetracker.domain.enumeration.Status;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.StatusMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mapstruct.factory.Mappers.getMapper;

public class StatusMapperTest {
    
    private final StatusMapper mapper = getMapper(StatusMapper.class);
    
    @Test
    void shouldMap() {
        // given
        final Status status = Status.RETURN;
        // when
		final com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status secondaryStatus = mapper
				.map(status);
        // then
        assertEquals(status.name(), secondaryStatus.name());
    }
}
