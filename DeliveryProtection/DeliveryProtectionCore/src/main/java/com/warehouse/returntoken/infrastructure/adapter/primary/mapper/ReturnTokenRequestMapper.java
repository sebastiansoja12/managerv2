package com.warehouse.returntoken.infrastructure.adapter.primary.mapper;

import com.warehouse.returntoken.domain.model.Parcel;
import com.warehouse.returntoken.infrastructure.adapter.primary.dto.ParcelDto;
import org.mapstruct.Mapper;

@Mapper
public interface ReturnTokenRequestMapper {
    Parcel map(ParcelDto parcelRequest);
}
