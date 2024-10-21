package com.warehouse.zebrareturn.infrastructure.adapter.secondary.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.vo.ProcessReturn;
import com.warehouse.zebrareturn.infrastructure.adapter.secondary.api.ProcessReturnDto;

@Mapper
public interface ReturnResponseMapper {
    List<ProcessReturn> map(List<ProcessReturnDto> processReturns);
}
