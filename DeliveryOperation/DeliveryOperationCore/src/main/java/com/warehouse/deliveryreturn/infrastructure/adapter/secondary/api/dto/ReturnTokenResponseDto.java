package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

import java.util.List;

public class ReturnTokenResponseDto {

    private List<ReturnPackageResponseDto> returnPackageResponses;

    private SupplierDto supplier;

    public ReturnTokenResponseDto() {
    }

    public ReturnTokenResponseDto(final List<ReturnPackageResponseDto> returnPackageResponses,
                                  final SupplierDto supplier) {
        this.returnPackageResponses = returnPackageResponses;
        this.supplier = supplier;
    }

    public List<ReturnPackageResponseDto> getReturnPackageResponses() {
        return returnPackageResponses;
    }

    public SupplierDto getSupplier() {
        return supplier;
    }
}
