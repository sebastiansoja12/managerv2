package com.warehouse.suppliertoken.domain.service;


import com.warehouse.suppliertoken.domain.model.*;
import com.warehouse.suppliertoken.domain.port.secondary.SupplierTokenServicePort;
import lombok.AllArgsConstructor;

import java.util.List;

import static com.google.common.collect.MoreCollectors.onlyElement;

@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierTokenServicePort supplierTokenServicePort;

    @Override
    public SupplierTokenResponse protect(SupplierTokenRequest request, List<Parcel> parcels) {
        final SupplierTokenRequest supplierTokenRequest = assignParcelValuesToDeliveryPackage(request, parcels);
        return supplierTokenServicePort.protect(supplierTokenRequest);
    }

	private SupplierTokenRequest assignParcelValuesToDeliveryPackage(SupplierTokenRequest request,
			List<Parcel> parcels) {
        final List<DeliveryPackageRequest> deliveryPackageRequests = request.getDeliveryPackageRequests()
                .stream()
                .map(deliveryPackageRequest -> {
                    final Parcel matchingParcel = parcels.stream()
                            .filter(parcel -> parcelExists(deliveryPackageRequest, parcel))
                            .collect(onlyElement());

                    return new DeliveryPackageRequest(matchingParcel, deliveryPackageRequest.getSupplier(),
                            deliveryPackageRequest.getDelivery());
                }).toList();
        return new SupplierTokenRequest(deliveryPackageRequests);
    }

    private boolean parcelExists(DeliveryPackageRequest deliveryPackageRequest, Parcel parcel) {
        return parcel.getId().equals(deliveryPackageRequest.getParcel().getId());
    }
}
