package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import com.warehouse.deliveryreturn.domain.vo.Parcel;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ParcelDto;
import org.mapstruct.Mapper;

@Mapper
public interface ParcelMapper {

    default Parcel map(ParcelDto parcel) {
        return Parcel.builder()
                .id(parcel.getParcelId().getValue())
                .parcelStatus(parcel.getParcelStatus().name())
                .recipientEmail(parcel.getRecipient().getEmail())
                .senderEmail(parcel.getSender().getEmail())
                .locked(parcel.getLocked())
                .build();
    }
}
