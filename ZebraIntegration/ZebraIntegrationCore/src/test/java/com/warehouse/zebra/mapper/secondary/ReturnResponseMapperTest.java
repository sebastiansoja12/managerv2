package com.warehouse.zebra.mapper.secondary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.warehouse.zebra.domain.vo.ProcessReturn;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.ProcessReturnDto;
import com.warehouse.zebra.infrastructure.adapter.secondary.mapper.ReturnResponseMapper;

public class ReturnResponseMapperTest {

    private ReturnResponseMapper mapper = getMapper(ReturnResponseMapper.class);

    @Test
    void shouldMapToProcessReturn() {
        // given
        final List<ProcessReturnDto> processReturnList = List.of(ProcessReturnDto.builder()
                .processStatus("OK")
                .build());
        // when
        final List<ProcessReturn> processReturns = mapper.map(processReturnList);
        // then
        assertThat(processReturns).extracting(ProcessReturn::processStatus).containsExactly("OK");
    }
}
