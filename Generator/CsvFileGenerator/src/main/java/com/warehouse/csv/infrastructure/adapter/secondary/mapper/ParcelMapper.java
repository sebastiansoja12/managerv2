package com.warehouse.csv.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.csv.domain.vo.ParcelCsv;
import com.warehouse.csv.infrastructure.adapter.secondary.entity.ParcelEntity;
import org.mapstruct.Mapping;

@Mapper
public interface ParcelMapper {

    @Mapping(target = "senderTelephoneNumber", source = "senderTelephone")
    @Mapping(target = "recipientTelephoneNumber", source = "recipientTelephone")
    ParcelCsv map(ParcelEntity parcel);
}
