package com.warehouse.parcelstatuschange.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.parcelstatuschange.domain.vo.Status;
import com.warehouse.parcelstatuschange.infrastructure.adapter.primary.api.dto.StatusDto;

@Mapper
public interface ParcelStatusResponseMapper {

    StatusDto map(Status status);

}
