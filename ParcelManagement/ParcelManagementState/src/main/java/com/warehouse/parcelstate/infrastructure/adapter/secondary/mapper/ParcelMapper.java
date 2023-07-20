package com.warehouse.parcelstate.infrastructure.adapter.secondary.mapper;

import com.warehouse.parcelstate.domain.model.Parcel;
import com.warehouse.parcelstate.infrastructure.adapter.secondary.entity.ParcelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParcelMapper {

    ParcelEntity map(Parcel parcel);
    Parcel map(ParcelEntity parcel);
}
