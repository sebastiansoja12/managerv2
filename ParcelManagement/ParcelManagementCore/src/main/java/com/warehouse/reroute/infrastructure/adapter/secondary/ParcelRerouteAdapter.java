package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.model.RerouteParcel;
import com.warehouse.reroute.domain.port.secondary.ParcelReroutePort;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.ParcelMapper;
import com.warehouse.shipment.infrastructure.api.ShipmentService;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelResponseDto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParcelRerouteAdapter implements ParcelReroutePort {

    private final ShipmentService shipmentService;

    private final ParcelMapper parcelMapper;

    // TODO refactor, add request and response for parcel in this domain
    @Override
    public Parcel reroute(RerouteParcel parcel, ParcelId parcelId) {
        final UpdateParcelRequestDto updateRequest = parcelMapper.map(parcel, parcelId);
        final UpdateParcelResponseDto updateResponse = shipmentService.update(updateRequest);
        return parcelMapper.map(updateResponse);
    }
}
