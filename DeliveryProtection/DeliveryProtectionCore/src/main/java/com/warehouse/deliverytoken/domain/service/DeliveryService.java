package com.warehouse.deliverytoken.domain.service;

import com.warehouse.deliverytoken.domain.model.DeliveryTokenResponse;
import com.warehouse.deliverytoken.domain.model.Parcel;
import com.warehouse.deliverytoken.domain.model.DeliveryTokenRequest;

import java.util.List;

public interface DeliveryService {
    DeliveryTokenResponse protect(DeliveryTokenRequest request, List<Parcel> parcels);
}
