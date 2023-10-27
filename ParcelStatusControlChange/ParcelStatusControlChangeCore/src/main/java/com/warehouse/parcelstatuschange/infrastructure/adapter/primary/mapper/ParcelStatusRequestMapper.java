package com.warehouse.parcelstatuschange.infrastructure.adapter.primary.mapper;

import com.warehouse.parcelstatuschange.domain.enumeration.Status;
import com.warehouse.parcelstatuschange.domain.model.Parcel;
import com.warehouse.parcelstatuschange.domain.model.StatusRequest;
import com.warehouse.parcelstatuschange.infrastructure.adapter.primary.api.dto.ParcelDto;
import com.warehouse.parcelstatuschange.infrastructure.adapter.primary.api.dto.StatusDto;
import com.warehouse.parcelstatuschange.infrastructure.adapter.primary.api.dto.StatusRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface ParcelStatusRequestMapper {
    default StatusRequest map(StatusRequestDto statusRequest) {
        final ParcelDto parcel = statusRequest.getParcel();
        return new StatusRequest(new Parcel(parcel.getParcelId().getValue(), map(parcel.getStatus())));
    }

    Status map(StatusDto status);


}
