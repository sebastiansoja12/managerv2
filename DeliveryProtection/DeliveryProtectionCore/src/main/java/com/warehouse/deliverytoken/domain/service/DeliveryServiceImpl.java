package com.warehouse.deliverytoken.domain.service;


import com.warehouse.deliverytoken.domain.model.*;
import com.warehouse.deliverytoken.domain.port.secondary.DeliveryTokenServicePort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryTokenServicePort deliveryTokenServicePort;

    @Override
    public DeliveryTokenResponse protect(DeliveryTokenRequest request, List<Parcel> parcels) {
        request.assignParcelValuesToDeliveryPackages(parcels);
        return deliveryTokenServicePort.protect(request);
    }
}
