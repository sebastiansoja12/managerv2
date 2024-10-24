package com.warehouse.zebra.mapper.primary;

import com.warehouse.commonassets.response.Response;
import com.warehouse.commonassets.vo.DeviceInformation;
import com.warehouse.zebra.infrastructure.adapter.primary.mapper.ZebraResponseMapper;
import com.warehouse.zebra.infrastructure.api.responsemodel.TerminalResponse;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mapstruct.factory.Mappers.getMapper;

public class TerminalResponseMapperTest {

    private final ZebraResponseMapper mapper = getMapper(ZebraResponseMapper.class);

    private final DeviceInformation deviceInformation = new DeviceInformation("1.0", 1L,
            "s-soja", "KT1");

    @Test
    void shouldMapToResponse() {
        // given
        final Response response = new Response(deviceInformation, Collections.emptyList(), Collections.emptyList());
        // when
        final TerminalResponse terminalResponse = mapper.map(response);
        // then
        assertEquals(1L, terminalResponse.getTerminalId());
    }
}
