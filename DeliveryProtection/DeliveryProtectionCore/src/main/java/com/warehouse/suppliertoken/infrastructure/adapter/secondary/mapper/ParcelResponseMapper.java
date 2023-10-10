package com.warehouse.suppliertoken.infrastructure.adapter.secondary.mapper;

import com.warehouse.suppliertoken.domain.enumeration.ParcelType;
import com.warehouse.suppliertoken.domain.model.Parcel;
import com.warehouse.suppliertoken.infrastructure.adapter.secondary.api.ParcelDto;
import com.warehouse.suppliertoken.infrastructure.adapter.secondary.api.enumeration.ParcelTypeDto;
import org.mapstruct.Mapper;

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
