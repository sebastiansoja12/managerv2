package com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.mapper;

import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.enumeration.Status;
import org.mapstruct.Mapper;

@Mapper
public interface StatusMapper {
    Status map(com.warehouse.parcelstatuschange.domain.vo.Status status);
}
