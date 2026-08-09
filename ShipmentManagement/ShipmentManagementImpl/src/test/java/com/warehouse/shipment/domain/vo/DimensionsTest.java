package com.warehouse.shipment.domain.vo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DimensionsTest {

    @Test
    void shouldAcceptPositiveDimensionsAndUnit() {
        assertDoesNotThrow(() -> new Dimensions(
                new BigDecimal("40"), new BigDecimal("30"), new BigDecimal("20"), LengthUnit.CM));
    }

    @Test
    void shouldRejectNullOrNonPositiveDimension() {
        assertThrows(IllegalArgumentException.class,
                () -> new Dimensions(null, BigDecimal.ONE, BigDecimal.ONE, LengthUnit.CM));
        assertThrows(IllegalArgumentException.class,
                () -> new Dimensions(BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE, LengthUnit.CM));
        assertThrows(IllegalArgumentException.class,
                () -> new Dimensions(BigDecimal.ONE, BigDecimal.valueOf(-1), BigDecimal.ONE, LengthUnit.CM));
        assertThrows(IllegalArgumentException.class,
                () -> new Dimensions(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO, LengthUnit.CM));
    }

    @Test
    void shouldRequireUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Dimensions(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, null));
    }
}
