package com.warehouse.zebra.mapper.primary;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mapstruct.factory.Mappers.getMapper;

import org.junit.jupiter.api.Test;

import com.warehouse.zebra.domain.vo.Request;
import com.warehouse.zebra.infrastructure.adapter.primary.mapper.ZebraRequestMapper;
import com.warehouse.zebra.infrastructure.api.requestmodel.ProcessType;
import com.warehouse.zebra.infrastructure.api.requestmodel.ZebraRequest;

public class ZebraRequestMapperTest {

    private final ZebraRequestMapper mapper = getMapper(ZebraRequestMapper.class);

    @Test
    void shouldMapToRequest() {
        // given
        final ZebraRequest zebraRequest = new ZebraRequest(ProcessType.CREATED, null, null, null);
        // when
        final Request request = mapper.map(zebraRequest);
        // then
        assertEquals(com.warehouse.zebra.domain.vo.ProcessType.CREATED, request.getProcessType());
    }
}
