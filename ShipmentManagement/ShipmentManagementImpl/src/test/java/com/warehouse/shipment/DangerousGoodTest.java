package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.domain.model.DangerousGood;

class DangerousGoodTest {

    @Test
    void shouldHaveValueObjectEquality() {
        assertEquals(DataTestCreator.dangerousGood(), DataTestCreator.dangerousGood());
        assertEquals(
                DataTestCreator.dangerousGood().hashCode(),
                DataTestCreator.dangerousGood().hashCode()
        );
    }

    @Test
    void shouldCreateCompleteDangerousGoodAndNormalizeText() {
        final DangerousGood dangerousGood = dangerousGood(" UN1203 ", BigDecimal.ONE, 1, " ");

        assertEquals("UN1203", dangerousGood.getUnNumber());
        assertEquals("Petrol", dangerousGood.getProperShippingName());
        assertEquals("ADR", dangerousGood.getRegulationType());
        assertEquals("ROAD", dangerousGood.getTransportMode());
        assertEquals(null, dangerousGood.getDescription());
    }

    @Test
    void shouldRejectInvalidUnNumber() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> dangerousGood("1203", BigDecimal.ONE, 1, null)
        );

        assertEquals(true, exception.getMessage().contains("UN number"));
    }

    @Test
    void shouldRejectNonPositiveQuantity() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> dangerousGood("UN1203", BigDecimal.ZERO, 1, null)
        );

        assertEquals(true, exception.getMessage().contains("quantity must be greater than zero"));
    }

    @Test
    void shouldRejectNonPositivePackageCount() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> dangerousGood("UN1203", BigDecimal.ONE, 0, null)
        );

        assertEquals(true, exception.getMessage().contains("package count must be greater than zero"));
    }

    @Test
    void shouldRequire24HourContactForAirTransport() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DangerousGood(
                        "UN1203", "Petrol", null, "3", null, null, "II", BigDecimal.ONE,
                        "LITRE", 1, "DRUM", false, false, false, false, null, null, null,
                        "112", null, "SDS-1", null, "IATA", "AIR", true, false, false,
                        null, null, null, CountryCode.PL
                )
        );

        assertEquals(true, exception.getMessage().contains("24-hour emergency contact"));
    }

    private DangerousGood dangerousGood(
            final String unNumber,
            final BigDecimal quantity,
            final Integer packageCount,
            final String description
    ) {
        return new DangerousGood(
                unNumber, " Petrol ", description, "3", null, null, "II", quantity,
                "LITRE", packageCount, "DRUM", false, false, false, false, null, null, null,
                "112", null, "SDS-1", null, "adr", "road", true, false, false,
                null, null, null, CountryCode.PL
        );
    }
}
