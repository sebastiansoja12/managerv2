package com.warehouse.returning.infrastructure.adapter.primary.mapper;


import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.domain.vo.ReturnModel;
import com.warehouse.returning.infrastructure.api.dto.ProcessReturnDto;
import com.warehouse.returning.infrastructure.api.dto.ReturnModelDto;
import org.mapstruct.Mapper;

import com.warehouse.returning.domain.vo.ReturnResponse;
import com.warehouse.returning.infrastructure.api.dto.ReturningResponseDto;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ReturnResponseMapper {
    default ReturningResponseDto map(ReturnResponse response) {
        return new ReturningResponseDto(map(response.processReturn()));
    }

    List<ProcessReturnDto> map(List<ProcessReturn> processReturn);


    @Mapping(target = "parcelId.value", source = "parcelId")
    @Mapping(target = "reason.value", source = "reason")
    @Mapping(target = "returnStatus", source = "returnStatus")
    @Mapping(target = "returnToken.value", source = "returnToken")
    @Mapping(target = "supplierCode.value", source = "supplierCode")
    @Mapping(target = "depotCode.value", source = "depotCode")
    @Mapping(target = "username.value", source = "username")
    ReturnModelDto map(ReturnModel returnModel);
}
