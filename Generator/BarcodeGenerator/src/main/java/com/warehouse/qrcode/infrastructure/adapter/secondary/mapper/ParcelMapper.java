package com.warehouse.qrcode.infrastructure.adapter.secondary.mapper;


import org.mapstruct.Mapper;

import com.warehouse.qrcode.domain.model.Parcel;
import com.warehouse.qrcode.infrastructure.adapter.secondary.entity.ParcelEntity;

@Mapper
public interface ParcelMapper {


    Parcel map(ParcelEntity parcelEntity);
}
