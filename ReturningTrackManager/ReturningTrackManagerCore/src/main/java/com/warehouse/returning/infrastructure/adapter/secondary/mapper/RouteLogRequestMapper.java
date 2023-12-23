package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ProcessTypeDto;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ReturnTrackRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface RouteLogRequestMapper {

    default ReturnTrackRequestDto map(ProcessReturn processReturn, String depotCode, String username) {
        return ReturnTrackRequestDto
                .builder()
                .username(username)
                .depotCode(depotCode)
                .processType(ProcessTypeDto.RETURN)
                .parcelId(processReturn.parcelId())
                .build();
    }
}
