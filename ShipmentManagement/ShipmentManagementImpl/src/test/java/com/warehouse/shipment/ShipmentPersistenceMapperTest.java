package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentPersistenceMapper;

class ShipmentPersistenceMapperTest {

    private final ShipmentPersistenceMapper mapper = new ShipmentPersistenceMapper();

    @Test
    void shouldRehydrateExistingStateWithoutChangingTimestamps() {
        final Shipment original = DataTestCreator.shipment();

        final ShipmentEntity entity = this.mapper.toEntity(original);
        final Shipment rehydrated = this.mapper.toDomain(entity);

        assertThat(rehydrated.getShipmentId()).isEqualTo(original.getShipmentId());
        assertThat(rehydrated.getShipmentStatus()).isEqualTo(original.getShipmentStatus());
        assertThat(rehydrated.getShipmentType()).isEqualTo(original.getShipmentType());
        assertThat(rehydrated.getCreatedAt()).isEqualTo(original.getCreatedAt());
        assertThat(rehydrated.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
        assertThat(rehydrated.getExternalShipmentId()).isEqualTo(original.getExternalShipmentId());
        assertThat(rehydrated.getTrackingNumber()).isEqualTo(original.getTrackingNumber());
    }
}
