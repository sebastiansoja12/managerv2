package com.warehouse.pickuppoint.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;
import com.warehouse.pickuppoint.domain.model.PickupPoint;
import com.warehouse.pickuppoint.domain.vo.PickupPointCode;
import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity.PickupPointEntity;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity.PickupPointReadEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PickupPointPersistenceMapperTest {

    private static final Instant TIMESTAMP = Instant.parse("2026-09-09T08:00:00Z");

    private final PickupPointPersistenceMapper mapper = new PickupPointPersistenceMapper();

    @Test
    void shouldMapIncompleteSuspendedPickupPointToReadModel() {
        final PickupPointReadEntity readEntity = this.mapper.toReadEntity(incompletePickupPoint().snapshot());

        assertEquals(PickupPointStatus.SUSPENDED, readEntity.getStatus());
        assertNull(readEntity.getCountryCode());
        assertNull(readEntity.getLatitude());
        assertTrue(readEntity.getAllowedShipmentSizes().isEmpty());
        assertFalse(readEntity.acceptsDangerousGoods());
    }

    @Test
    void shouldMapIncompleteSuspendedPickupPointToEntity() {
        final PickupPointEntity entity = this.mapper.toEntityForUpdate(incompletePickupPoint());

        assertNull(entity.getCountryCode());
        assertNull(entity.getLatitude());
        assertNull(entity.getOpeningTimeZone());
        assertTrue(entity.getAllowedShipmentSizes().isEmpty());
    }

    private static PickupPoint incompletePickupPoint() {
        return PickupPoint.restore(new PickupPointSnapshot(
                new PickupPointId(UUID.fromString("11f6a9b0-e529-4ff4-bf40-865d246f6138")),
                new PickupPointCode("WAW-PP-002"),
                "Imported point",
                PickupPointType.PARCEL_LOCKER,
                PickupPointStatus.SUSPENDED,
                Set.of(PickupPointCapability.COLLECTION),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Missing address",
                3,
                TIMESTAMP,
                TIMESTAMP));
    }
}
