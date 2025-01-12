package com.warehouse.deliveryreturn.domain.vo;

import java.util.List;


public class ReturnTokenResponse {
    private final List<ReturnPackageResponse> returnPackageResponses;
    private final Supplier supplier;

    public ReturnTokenResponse(final List<ReturnPackageResponse> returnPackageResponses, final Supplier supplier) {
        this.returnPackageResponses = returnPackageResponses;
        this.supplier = supplier;
    }

    public List<ReturnPackageResponse> getDeliveryReturnSignatures() {
        return returnPackageResponses;
    }

    public Supplier getSupplier() {
        return supplier;
    }
}
