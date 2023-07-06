package com.warehouse.qrcode.infrastructure.adapter.primary.mapper;

import com.warehouse.qrcode.domain.model.Parcel;
import org.mapstruct.Mapper;

@Mapper
public interface ParcelEntityMapper {

    Parcel map(com.warehouse.shipment.domain.model.Parcel parcel);
}
