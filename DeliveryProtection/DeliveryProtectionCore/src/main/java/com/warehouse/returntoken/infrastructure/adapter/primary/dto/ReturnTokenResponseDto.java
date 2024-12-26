package com.warehouse.returntoken.infrastructure.adapter.primary.dto;

import java.util.List;


public class ReturnTokenResponseDto {

    private List<ReturnPackageResponseDto> deliveryReturnSignatures;

    private SupplierDto supplier;

    public ReturnTokenResponseDto() {
    }

    public ReturnTokenResponseDto(final List<ReturnPackageResponseDto> deliveryReturnSignatures,
                                  final SupplierDto supplier) {
        this.deliveryReturnSignatures = deliveryReturnSignatures;
        this.supplier = supplier;
    }

    public List<ReturnPackageResponseDto> getDeliveryReturnSignatures() {
        return deliveryReturnSignatures;
    }

    public SupplierDto getSupplier() {
        return supplier;
    }
}
