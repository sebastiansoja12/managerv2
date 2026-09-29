package com.warehouse.shipment.domain.vo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeightTest {

    @Test
    void shouldAcceptPositiveValueAndUnit() {
        assertDoesNotThrow(() -> new Weight(new BigDecimal("5.5"), WeightUnit.KG));
    }

    @Test
    void shouldRejectNullOrNonPositiveValue() {
        assertThrows(IllegalArgumentException.class, () -> new Weight(null, WeightUnit.KG));
        assertThrows(IllegalArgumentException.class, () -> new Weight(BigDecimal.ZERO, WeightUnit.KG));
        assertThrows(IllegalArgumentException.class, () -> new Weight(BigDecimal.valueOf(-1), WeightUnit.KG));
    }

    @Test
    void shouldRequireUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Weight(BigDecimal.ONE, null));
    }
}
