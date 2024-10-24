package com.warehouse.zebra.mapper.primary;


import static com.warehouse.commonassets.enumeration.ProcessType.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.commonassets.request.Request;
import com.warehouse.zebra.infrastructure.adapter.primary.mapper.ZebraRequestMapper;
import com.warehouse.zebra.infrastructure.api.requestmodel.ProcessType;
import com.warehouse.zebra.infrastructure.api.requestmodel.TerminalRequest;
import org.junit.jupiter.api.Test;


public class TerminalRequestMapperTest {

    private final ZebraRequestMapper mapper = getMapper(ZebraRequestMapper.class);

    @Test
    void shouldMapToRequest() {
        // given
        final TerminalRequest terminalRequest = new TerminalRequest(ProcessType.CREATED, null, null, null);
        // when
        final Request request = mapper.map(terminalRequest);
        // then
        assertEquals(CREATED, request.getProcessType());
    }
}
