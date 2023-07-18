package com.warehouse.parcelstate.infrastructure.adapter.primary.mapper;

import com.warehouse.parcelstate.domain.model.Parcel;
import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.ParcelDto;
import org.mapstruct.Mapper;

@Mapper
public interface ParcelStateRequestMapper {

    Parcel map(ParcelDto parcel);
}
