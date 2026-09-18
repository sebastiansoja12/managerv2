package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShipmentDeliveryDetailsTest {

    @Test
    void shouldKeepOriginDepartmentOfFirstDelivery() {
        final LocalDateTime deliveredAt = LocalDateTime.of(2026, 10, 29, 12, 0);
        final ShipmentDeliveryDetails current = new ShipmentDeliveryDetails(new DepartmentId(1L),
                new DepartmentId(5L), null, null);
        final ShipmentDeliveryDetails continuation = new ShipmentDeliveryDetails(new DepartmentId(8L),
                new DepartmentId(5L), null, deliveredAt);

        assertEquals(new ShipmentDeliveryDetails(new DepartmentId(1L), new DepartmentId(5L), null, deliveredAt),
                continuation.keepingOriginOf(current));
    }

    @Test
    void shouldTakeOriginDepartmentWhenNoneKnownYet() {
        final ShipmentDeliveryDetails details = new ShipmentDeliveryDetails(new DepartmentId(1L),
                new DepartmentId(5L), null, null);

        assertEquals(details, details.keepingOriginOf(new ShipmentDeliveryDetails(null, null, null, null)));
    }
}
