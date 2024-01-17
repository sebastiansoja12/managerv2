package com.warehouse.zebra.mapper.secondary;

import com.warehouse.zebra.domain.vo.DeviceInformation;
import com.warehouse.zebra.domain.vo.Request;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.ReturnRequestDto;
import com.warehouse.zebra.infrastructure.adapter.secondary.mapper.ReturnRequestMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mapstruct.factory.Mappers.getMapper;

public class ReturnRequestMapperTest {

    private final ReturnRequestMapper mapper = getMapper(ReturnRequestMapper.class);

    private final DeviceInformation deviceInformation = DeviceInformation.builder()
            .depotCode("KT1")
            .username("s-soja")
            .version("1.0")
            .zebraId(1L)
            .build();

    @Test
    void shouldMapToReturnRequestDto() {
        // given
        final Request request = Request.builder().zebraDeviceInformation(deviceInformation).build();
        // when
        final ReturnRequestDto requestDto = mapper.map(request);
        // then
        assertEquals("s-soja", requestDto.getUsername().getValue());
    }
}
