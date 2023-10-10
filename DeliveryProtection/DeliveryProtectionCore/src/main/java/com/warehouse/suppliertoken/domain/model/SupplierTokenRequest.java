package com.warehouse.suppliertoken.domain.model;

import lombok.Value;

import java.util.List;
import java.util.Objects;


@Value
public class SupplierTokenRequest {

    List<DeliveryPackageRequest> deliveryPackageRequests;


    public List<ParcelId> extractParcelIds() {
        return deliveryPackageRequests.stream()
                .map(DeliveryPackageRequest::getParcel)
                .map(Parcel::getId)
                .map(ParcelId::new)
                .toList();
    }

    public boolean validateDeliveryPackage() {
        return deliveryPackageRequests.stream()
                .map(DeliveryPackageRequest::getParcel)
                .anyMatch(parcel -> Objects.isNull(parcel.getId()));
    }
}