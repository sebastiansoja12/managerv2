package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryReadItem;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryTargetType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import org.junit.jupiter.api.Test;

import static com.warehouse.logisticsdelivery.DeliveryFixture.DESTINATION_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.ORIGIN_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.USER_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.shipmentDelivery;
import static org.assertj.core.api.Assertions.assertThat;

class DeliveryReadModelMapperTest {

    private final DeliveryReadModelMapper mapper = new DeliveryReadModelMapper();

    @Test
    void shouldProjectDeliverySnapshotIntoReadItem() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.complete(null, USER_ID, null);

        final DeliveryReadItem item = this.mapper.toItem(this.mapper.toEntity(delivery.snapshot()));

        assertThat(item.deliveryId()).isEqualTo(delivery.getDeliveryId());
        assertThat(item.shipmentId()).isEqualTo(new ShipmentId(10L));
        assertThat(item.targetType()).isEqualTo(DeliveryTargetType.SHIPMENT);
        assertThat(item.targetId()).isEqualTo("10");
        assertThat(item.type()).isEqualTo(DeliveryType.OUTBOUND);
        assertThat(item.deliveryStatus()).isEqualTo(DeliveryStatus.DELIVERED);
        assertThat(item.status()).isEqualTo(DeliveryLifecycleStatus.COMPLETED);
        assertThat(item.originDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(item.destinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(item.method()).isEqualTo(DeliveryMethod.LOCKER);
        assertThat(item.stepCount()).isEqualTo(2);
        assertThat(item.createdAt()).isEqualTo(delivery.getCreatedAt());
        assertThat(item.deliveredAt()).isEqualTo(delivery.getDeliveredAt());
    }
}
