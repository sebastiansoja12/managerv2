package com.warehouse.deliverytoken.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.deliverytoken.domain.enumeration.ParcelType;
import com.warehouse.deliverytoken.domain.model.Parcel;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.api.dto.ParcelDto;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.api.enumeration.ParcelTypeDto;

@Mapper
public interface ParcelResponseMapper {



    default Parcel map(ParcelDto parcel) {
        return new Parcel(
                parcel.getParcelId().getValue(),
                parcel.getParcelRelatedId(),
                mapToParcelType(parcel.getParcelType()),
                parcel.getDestination()
        );
    }

    default ParcelType mapToParcelType(ParcelTypeDto parcelType) {
        return switch (parcelType) {
            case PARENT -> ParcelType.PARENT;
            case CHILD -> ParcelType.CHILD;
        };
    }
}
