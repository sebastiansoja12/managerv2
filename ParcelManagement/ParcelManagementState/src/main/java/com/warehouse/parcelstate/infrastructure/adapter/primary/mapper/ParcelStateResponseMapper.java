package com.warehouse.parcelstate.infrastructure.adapter.primary.mapper;

import com.warehouse.parcelstate.domain.model.RerouteParcel;
import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.RerouteParcelDto;
import org.mapstruct.Mapper;

@Mapper
public interface ParcelStateResponseMapper {

    RerouteParcelDto map(RerouteParcel rerouteParcel);
}
