package com.warehouse.parcelstate.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.parcelstate.domain.model.RerouteParcel;
import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.RerouteRequestDto;

@Mapper
public interface ParcelStateRequestMapper {

    RerouteParcel map(RerouteRequestDto parcel);
}
