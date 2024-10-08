package com.warehouse.deliverytoken.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.enumeration.ShipmentType;
import org.mapstruct.Mapper;

import com.warehouse.deliverytoken.domain.vo.Parcel;
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

    default ShipmentType mapToParcelType(ParcelTypeDto parcelType) {
        return switch (parcelType) {
            case PARENT -> ShipmentType.PARENT;
            case CHILD -> ShipmentType.CHILD;
        };
    }
}
