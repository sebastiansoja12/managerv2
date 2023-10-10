package com.warehouse.suppliertoken.domain.port.primary;


import com.warehouse.suppliertoken.domain.exception.MissingParcelIdException;
import com.warehouse.suppliertoken.domain.model.Parcel;
import com.warehouse.suppliertoken.domain.model.ParcelId;
import com.warehouse.suppliertoken.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.domain.model.SupplierTokenResponse;
import com.warehouse.suppliertoken.domain.port.secondary.ParcelServicePort;
import com.warehouse.suppliertoken.domain.service.SupplierService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SupplierTokenPortImpl implements SupplierTokenPort {

    private final SupplierService service;

    private final ParcelServicePort parcelServicePort;

    @Override
    public SupplierTokenResponse protect(SupplierTokenRequest request) {


        final boolean requestValidation = request.validateDeliveryPackage();

        if (requestValidation) {
            throw new MissingParcelIdException("Missing parcel in request");
        }

        final List<ParcelId> parcelIds = request.extractParcelIds();

        final List<Parcel> parcels = parcelServicePort.downloadParcels(parcelIds);

        return service.protect(request, parcels);
    }
}
