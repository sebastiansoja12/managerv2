package com.warehouse.deliveryreturn.domain.vo;

import com.warehouse.commonassets.identificator.SupplierCode;

import java.util.List;


public class ReturnTokenResponse {
    private final List<DeliveryReturnSignature> deliveryReturnSignatures;
    private final SupplierCode supplierCode;

    public ReturnTokenResponse(final List<DeliveryReturnSignature> deliveryReturnSignatures,
                               final SupplierCode supplierCode) {
        this.deliveryReturnSignatures = deliveryReturnSignatures;
        this.supplierCode = supplierCode;
    }

    public List<DeliveryReturnSignature> getDeliveryReturnSignatures() {
        return deliveryReturnSignatures;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }
}
