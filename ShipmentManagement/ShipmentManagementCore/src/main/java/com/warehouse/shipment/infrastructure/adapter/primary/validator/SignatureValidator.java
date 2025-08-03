package com.warehouse.shipment.infrastructure.adapter.primary.validator;

import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.model.Signature;

public class SignatureValidator {
    public void validateSignature(final Signature signature) {

    }

    public static void validateSignatureMethod(final String signatureMethod) {
        try {
            SignatureMethod.valueOf(signatureMethod);
        } catch (final IllegalArgumentException e) {
            throw new RestException(400, "Invalid signature method: " + signatureMethod);
        }
    }
}
