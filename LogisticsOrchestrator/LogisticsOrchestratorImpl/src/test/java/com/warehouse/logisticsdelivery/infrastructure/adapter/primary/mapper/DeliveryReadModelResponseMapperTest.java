package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryReadItem;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryReadPage;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryTargetType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryReadItemResponseDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryReadPageResponseDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.warehouse.logisticsdelivery.DeliveryFixture.DESTINATION_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.ORIGIN_DEPARTMENT_ID;
import static org.assertj.core.api.Assertions.assertThat;

class DeliveryReadModelResponseMapperTest {

    @Test
    void shouldMapReadPageToResponse() {
        final LocalDateTime createdAt = LocalDateTime.of(2026, 10, 20, 9, 0);
        final DeliveryReadPage page = new DeliveryReadPage(List.of(new DeliveryReadItem(new DeliveryId("delivery-1"),
                new ShipmentId(10L), DeliveryTargetType.SHIPMENT, "10", DeliveryType.OUTBOUND, DeliveryStatus.DEPOT,
                DeliveryLifecycleStatus.CREATED, ORIGIN_DEPARTMENT_ID, DESTINATION_DEPARTMENT_ID,
                DeliveryMethod.COURIER, 1, createdAt, null)), 2, 25, true);

        final DeliveryReadPageResponseDto response = new DeliveryReadModelResponseMapper().map(page);

        assertThat(response.page()).isEqualTo(2);
        assertThat(response.size()).isEqualTo(25);
        assertThat(response.hasNext()).isTrue();
        final DeliveryReadItemResponseDto item = response.items().get(0);
        assertThat(item.id()).isEqualTo("delivery-1");
        assertThat(item.shipmentId()).isEqualTo("10");
        assertThat(item.targetType()).isEqualTo("SHIPMENT");
        assertThat(item.deliveryStatus()).isEqualTo("DEPOT");
        assertThat(item.lifecycleStatus()).isEqualTo("CREATED");
        assertThat(item.originDepartmentId()).isEqualTo("3");
        assertThat(item.destinationDepartmentId()).isEqualTo("5");
        assertThat(item.method()).isEqualTo("COURIER");
        assertThat(item.stepCount()).isEqualTo(1);
        assertThat(item.createdAt()).isEqualTo(createdAt);
    }
}
