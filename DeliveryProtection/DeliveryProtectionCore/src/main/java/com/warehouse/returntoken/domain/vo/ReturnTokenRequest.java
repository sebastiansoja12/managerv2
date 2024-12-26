package com.warehouse.returntoken.domain.vo;


import java.util.List;

import com.warehouse.returntoken.domain.model.ReturnPackageRequest;

import lombok.Builder;


@Builder
public class ReturnTokenRequest {
    private final List<ReturnPackageRequest> returnPackageRequests;
    private final Supplier supplier;

    public ReturnTokenRequest(final List<ReturnPackageRequest> returnPackageRequests, final Supplier supplier) {
        this.returnPackageRequests = returnPackageRequests;
        this.supplier = supplier;
    }

    public List<ReturnPackageRequest> getReturnPackageRequests() {
        return returnPackageRequests;
    }

    public Supplier getSupplier() {
        return supplier;
    }

}
