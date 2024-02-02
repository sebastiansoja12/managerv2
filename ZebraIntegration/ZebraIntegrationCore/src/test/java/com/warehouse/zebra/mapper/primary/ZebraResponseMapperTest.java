package com.warehouse.zebra.mapper.primary;

import com.warehouse.zebra.domain.vo.Response;
import com.warehouse.zebra.infrastructure.adapter.primary.mapper.ZebraResponseMapper;
import com.warehouse.zebra.infrastructure.api.responsemodel.ZebraResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mapstruct.factory.Mappers.getMapper;

public class ZebraResponseMapperTest {

    private final ZebraResponseMapper mapper = getMapper(ZebraResponseMapper.class);

    @Test
    void shouldMapToResponse() {
        // given
        final Response response = Response.builder().zebraId(1L).build();
        // when
        final ZebraResponse zebraResponse = mapper.map(response);
        // then
        assertEquals(1L, zebraResponse.getZebraId());
    }
}
