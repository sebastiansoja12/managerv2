package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

import java.util.List;

public class ReturnTokenRequestDto {
    private List<ReturnPackageRequestDto> returnPackageRequests;
    private SupplierDto supplier;

    public ReturnTokenRequestDto() {
    }

    public ReturnTokenRequestDto(final List<ReturnPackageRequestDto> returnPackageRequests, final SupplierDto supplier) {
        this.returnPackageRequests = returnPackageRequests;
        this.supplier = supplier;
    }

    public List<ReturnPackageRequestDto> getReturnPackageRequests() {
        return returnPackageRequests;
    }

    public SupplierDto getSupplier() {
        return supplier;
    }
}
