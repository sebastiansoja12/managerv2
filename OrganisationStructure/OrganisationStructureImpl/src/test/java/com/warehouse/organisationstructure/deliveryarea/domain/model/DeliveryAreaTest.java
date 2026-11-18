package com.warehouse.organisationstructure.deliveryarea.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.domain.enumeration.DeliveryAreaSource;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaBoundary;
import com.warehouse.commonassets.identificator.DeliveryAreaId;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaName;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.GeoPoint;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryAreaTest {

    private static final Instant CREATED_AT = Instant.parse("2026-10-28T10:00:00Z");

    private static final Instant UPDATED_AT = Instant.parse("2026-10-28T11:00:00Z");

    private static final DeliveryAreaBoundary BOUNDARY = new DeliveryAreaBoundary(List.of(
            new GeoPoint(52.0, 21.0), new GeoPoint(52.0, 21.1), new GeoPoint(52.1, 21.1)));

    @Test
    void shouldKeepAutomaticSourceWhenUpdatedWithoutChanges() {
        final DeliveryArea deliveryArea = automaticArea();

        deliveryArea.update(new DeliveryAreaName("KT1 · 1"), BOUNDARY, UPDATED_AT);

        assertThat(deliveryArea.getSource()).isEqualTo(DeliveryAreaSource.AUTOMATIC);
        assertThat(deliveryArea.getUpdatedAt()).isEqualTo(CREATED_AT);
    }

    @Test
    void shouldBecomeManualWhenBoundaryChanges() {
        final DeliveryArea deliveryArea = automaticArea();
        final DeliveryAreaBoundary changed = new DeliveryAreaBoundary(List.of(
                new GeoPoint(52.0, 21.0), new GeoPoint(52.0, 21.2), new GeoPoint(52.1, 21.1)));

        deliveryArea.update(new DeliveryAreaName("KT1 · 1"), changed, UPDATED_AT);

        assertThat(deliveryArea.getSource()).isEqualTo(DeliveryAreaSource.MANUAL);
        assertThat(deliveryArea.getBoundary()).isEqualTo(changed);
        assertThat(deliveryArea.getUpdatedAt()).isEqualTo(UPDATED_AT);
    }

    private DeliveryArea automaticArea() {
        return DeliveryArea.rehydrate(new DeliveryAreaId(1L), new DepartmentId(10L), new DeliveryAreaName("KT1 · 1"),
                BOUNDARY, DeliveryAreaSource.AUTOMATIC, CREATED_AT, CREATED_AT);
    }
}
