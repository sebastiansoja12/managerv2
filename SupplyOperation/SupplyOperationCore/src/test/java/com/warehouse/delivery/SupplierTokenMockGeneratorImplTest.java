package com.warehouse.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.warehouse.delivery.domain.model.SupplierTokenRequest;
import com.warehouse.delivery.infrastructure.adapter.secondary.SupplierTokenMockGeneratorImpl;

public class SupplierTokenMockGeneratorImplTest {

    private final SupplierTokenMockGeneratorImpl generator = new SupplierTokenMockGeneratorImpl();


    @Test
    void shouldGenerateBase64Token() {
        // given
        final SupplierTokenRequest request = null;//new SupplierTokenRequest("abc_def",
                //UUID.fromString("fde44928-44e5-11ee-be56-0242ac120002"));
        // when
        final String token = generator.generateToken(request);
        // then
        assertEquals(expectedToBe("YWJjX2RlZmZkZTQ0OTI4LTQ0ZTUtMTFlZS1iZTU2LTAyNDJhYzEyMDAwMg=="), token);
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
