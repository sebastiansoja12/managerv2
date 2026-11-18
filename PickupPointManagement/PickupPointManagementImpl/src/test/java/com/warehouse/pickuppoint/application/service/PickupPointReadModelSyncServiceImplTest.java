package com.warehouse.pickuppoint.application.service;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointReadModelRepository;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointRepository;
import com.warehouse.pickuppoint.domain.enumeration.OpeningScheduleMode;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointShipmentSize;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;
import com.warehouse.pickuppoint.domain.model.PickupPoint;
import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.OpeningSchedule;
import com.warehouse.pickuppoint.domain.vo.PickupPointAddress;
import com.warehouse.pickuppoint.domain.vo.PickupPointCode;
import com.warehouse.pickuppoint.domain.vo.PickupPointServicePolicy;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class PickupPointReadModelSyncServiceImplTest {

    private static final Instant CREATED_AT = Instant.parse("2026-09-09T08:00:00Z");

    private final PickupPointRepository pickupPointRepository = mock(PickupPointRepository.class);
    private final PickupPointReadModelRepository pickupPointReadModelRepository =
            mock(PickupPointReadModelRepository.class);
    private final PickupPointReadModelSyncServiceImpl service =
            new PickupPointReadModelSyncServiceImpl(pickupPointRepository, pickupPointReadModelRepository);

    @Test
    void shouldSyncAllPickupPointsIntoReadModel() {
        final PickupPoint first = pickupPoint("9a7f3b38-73d0-4e9b-91b7-dadfeab4882f", "WAW-PP-001");
        final PickupPoint second = pickupPoint("1c2f8e0a-5b7d-4b8e-9f1a-3d6c2b9e4a10", "WAW-PP-002");
        when(pickupPointRepository.findAll()).thenReturn(List.of(first, second));

        final int synced = service.syncReadModels();

        assertThat(synced).isEqualTo(2);
        verify(pickupPointReadModelRepository).sync(first.snapshot());
        verify(pickupPointReadModelRepository).sync(second.snapshot());
    }

    @Test
    void shouldReturnZeroWhenThereAreNoPickupPoints() {
        when(pickupPointRepository.findAll()).thenReturn(List.of());

        final int synced = service.syncReadModels();

        assertThat(synced).isZero();
        verifyNoInteractions(pickupPointReadModelRepository);
    }

    private static PickupPoint pickupPoint(final String pickupPointId, final String code) {
        return PickupPoint.create(
                new PickupPointId(UUID.fromString(pickupPointId)),
                new PickupPointCode(code),
                "Point by the station",
                PickupPointType.SERVICE_POINT,
                Set.of(PickupPointCapability.DROP_OFF, PickupPointCapability.COLLECTION),
                new PickupPointAddress(CountryCode.PL, "00-001", "Warsaw", "Example", "10", null),
                new GeoCoordinates(52.2297, 21.0122),
                new DepartmentId(10L),
                null,
                null,
                new OpeningSchedule(
                        ZoneId.of("Europe/Warsaw"),
                        OpeningScheduleMode.ALWAYS_OPEN,
                        List.of(),
                        List.of()),
                new PickupPointServicePolicy(
                        Set.of(PickupPointShipmentSize.SMALL, PickupPointShipmentSize.MEDIUM),
                        false),
                null,
                CREATED_AT);
    }
}
