package com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.parcelstatuschange.domain.vo.Parcel;
import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.enumeration.Status;

@Mapper
public interface StatusMapper {
    Status map(com.warehouse.parcelstatuschange.domain.vo.Status status);

    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", source = "parcelStatus")
    ParcelEntity map(Parcel parcel);
}
