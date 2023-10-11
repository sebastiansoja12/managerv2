package com.warehouse.parcelstate.domain.port.primary;

import com.warehouse.parcelstate.domain.model.DeliveryStateRequest;
import com.warehouse.parcelstate.domain.model.DeliveryStateResponse;
import com.warehouse.parcelstate.domain.port.secondary.ParcelStateRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParcelStatePortImpl implements ParcelStatePort {

    private final ParcelStateRepository parcelStateRepository;

    @Override
    public DeliveryStateResponse updateStatus(DeliveryStateRequest request) {
        return null;
    }
}
