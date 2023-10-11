package com.warehouse.deliverytoken.domain.model;

import static com.google.common.collect.MoreCollectors.onlyElement;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class DeliveryTokenRequest {

    List<DeliveryPackageRequest> deliveryPackageRequests;


    public String extractSupplierCode() {
        return deliveryPackageRequests.stream()
                .map(DeliveryPackageRequest::getSupplier)
                .map(Supplier::getSupplierCode)
                .collect(Collectors.joining());
    }

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
    
    public void assignParcelValuesToDeliveryPackages(List<Parcel> parcels) {
		deliveryPackageRequests = deliveryPackageRequests.stream()
                .map(deliveryPackageRequest -> {
			final Parcel matchingParcel = parcels.stream()
					.filter(parcel -> parcelExists(deliveryPackageRequest, parcel))
                    .collect(onlyElement());
			return new DeliveryPackageRequest(matchingParcel, deliveryPackageRequest.getSupplier(),
					deliveryPackageRequest.getDelivery());
		}).toList();
    }

    private boolean parcelExists(DeliveryPackageRequest deliveryPackageRequest, Parcel parcel) {
        return parcel.getId().equals(deliveryPackageRequest.getParcel().getId());
    }
}