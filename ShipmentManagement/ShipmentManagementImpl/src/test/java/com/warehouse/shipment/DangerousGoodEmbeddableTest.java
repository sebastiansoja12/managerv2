package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.dangerousGood;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.DangerousGoodEmbeddable;
import jakarta.persistence.Embeddable;
import org.junit.jupiter.api.Test;

class DangerousGoodEmbeddableTest {

    @Test
    void shouldRoundTripDangerousGoodWithoutLosingData() {
        final DangerousGood source = dangerousGood();

        final DangerousGood restored = DangerousGoodEmbeddable.from(source).toDomain();

        assertEquals(source.getUnNumber(), restored.getUnNumber());
        assertEquals(source.getProperShippingName(), restored.getProperShippingName());
        assertEquals(source.getQuantity(), restored.getQuantity());
        assertEquals(source.getPackageCount(), restored.getPackageCount());
        assertEquals(source.getRegulationType(), restored.getRegulationType());
        assertEquals(source.getTransportMode(), restored.getTransportMode());
        assertEquals(source.isCorrosive(), restored.isCorrosive());
        assertEquals(source.getCountryOfOrigin(), restored.getCountryOfOrigin());
    }

    @Test
    void shouldBeJpaEmbeddable() {
        assertNotNull(DangerousGoodEmbeddable.class.getAnnotation(Embeddable.class));
    }
}
