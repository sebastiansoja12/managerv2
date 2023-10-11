package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.domain.model.SupplierTokenRequest;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class SupplierTokenMockGeneratorImpl implements SupplierTokenMockGenerator {
    @Override
    public String generateToken(SupplierTokenRequest request) {
        final String inputString =  request.getSupplierCode() + request.getRequestId();
        try {
            final byte[] inputBytes = inputString.getBytes(StandardCharsets.UTF_8);
            final byte[] base64Bytes = Base64.getEncoder().encode(inputBytes);
            return new String(base64Bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }
}
