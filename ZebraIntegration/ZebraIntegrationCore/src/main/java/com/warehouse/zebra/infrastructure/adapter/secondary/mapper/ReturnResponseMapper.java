package com.warehouse.zebra.infrastructure.adapter.secondary.mapper;

import com.warehouse.zebra.domain.vo.ProcessReturn;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.ProcessReturnDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ReturnResponseMapper {
    List<ProcessReturn> map(List<ProcessReturnDto> processReturns);
}
