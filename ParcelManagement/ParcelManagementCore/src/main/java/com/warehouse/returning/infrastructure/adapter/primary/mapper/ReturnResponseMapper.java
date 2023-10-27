package com.warehouse.returning.infrastructure.adapter.primary.mapper;


import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.infrastructure.api.dto.ProcessReturnDto;
import org.mapstruct.Mapper;

import com.warehouse.returning.domain.vo.ReturnResponse;
import com.warehouse.returning.infrastructure.api.dto.ReturningResponseDto;

import java.util.List;

@Mapper
public interface ReturnResponseMapper {
    default ReturningResponseDto map(ReturnResponse response) {
        return new ReturningResponseDto(map(response.processReturn()));
    }

    List<ProcessReturnDto> map(List<ProcessReturn> processReturn);


}
