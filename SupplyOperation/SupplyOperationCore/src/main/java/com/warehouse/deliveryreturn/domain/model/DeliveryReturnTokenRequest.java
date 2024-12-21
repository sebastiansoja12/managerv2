package com.warehouse.deliveryreturn.domain.model;


import java.util.List;

import com.warehouse.deliveryreturn.domain.vo.DeliveryPackageRequest;
import com.warehouse.deliveryreturn.domain.vo.Supplier;

import lombok.Builder;


@Builder
public class DeliveryReturnTokenRequest {
    private List<DeliveryPackageRequest> requests;
    private Supplier supplier;

    public DeliveryReturnTokenRequest(final List<DeliveryPackageRequest> requests, final Supplier supplier) {
        this.requests = requests;
        this.supplier = supplier;
    }

    public List<DeliveryPackageRequest> getRequests() {
        return requests;
    }

    public void setRequests(final List<DeliveryPackageRequest> requests) {
        this.requests = requests;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(final Supplier supplier) {
        this.supplier = supplier;
    }
}
