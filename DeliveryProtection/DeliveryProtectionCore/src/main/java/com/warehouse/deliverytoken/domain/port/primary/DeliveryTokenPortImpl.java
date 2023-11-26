package com.warehouse.deliverytoken.domain.port.primary;


import com.warehouse.deliverytoken.domain.exception.MissingParcelIdException;
import com.warehouse.deliverytoken.domain.vo.Parcel;
import com.warehouse.deliverytoken.domain.vo.ParcelId;
import com.warehouse.deliverytoken.domain.model.DeliveryTokenRequest;
import com.warehouse.deliverytoken.domain.vo.DeliveryTokenResponse;
import com.warehouse.deliverytoken.domain.port.secondary.ParcelServicePort;
import com.warehouse.deliverytoken.domain.service.DeliveryService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DeliveryTokenPortImpl implements DeliveryTokenPort {

    private final DeliveryService service;

    private final ParcelServicePort parcelServicePort;

    @Override
    public DeliveryTokenResponse protect(DeliveryTokenRequest request) {


        final boolean requestValidation = request.validateDeliveryPackage();

        if (requestValidation) {
            throw new MissingParcelIdException("Missing parcel in request");
        }

        final List<ParcelId> parcelIds = request.extractParcelIds();

        final List<Parcel> parcels = parcelIds.stream()
                .map(parcelServicePort::downloadParcel)
                .toList();

        return service.protect(request, parcels);
    }
}
