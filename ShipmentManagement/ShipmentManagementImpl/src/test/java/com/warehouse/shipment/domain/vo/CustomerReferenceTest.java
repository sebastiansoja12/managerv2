package com.warehouse.shipment.domain.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerReferenceTest {

    @Test
    void shouldAcceptNonBlankValue() {
        assertDoesNotThrow(() -> new CustomerReference("ORDER-2026-12345"));
    }

    @Test
    void shouldRejectNullOrBlankValue() {
        assertThrows(IllegalArgumentException.class, () -> new CustomerReference(null));
        assertThrows(IllegalArgumentException.class, () -> new CustomerReference("  \t"));
    }
}
