package com.warehouse.pickuppoint.domain.model;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.domain.enumeration.OpeningScheduleMode;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointShipmentSize;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;
import com.warehouse.pickuppoint.domain.exception.PickupPointUnavailableException;
import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.OpeningSchedule;
import com.warehouse.pickuppoint.domain.vo.PickupPointAddress;
import com.warehouse.pickuppoint.domain.vo.PickupPointCode;
import com.warehouse.pickuppoint.domain.vo.PickupPointSelectionCriteria;
import com.warehouse.pickuppoint.domain.vo.PickupPointServicePolicy;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PickupPointTest {

    private static final Instant CREATED_AT = Instant.parse("2026-09-09T08:00:00Z");
    private static final Instant UPDATED_AT = Instant.parse("2026-09-09T09:00:00Z");

    @Test
    void shouldCreateActivePickupPoint() {
        final PickupPoint pickupPoint = activePickupPoint();

        assertEquals(PickupPointStatus.ACTIVE, pickupPoint.snapshot().status());
        assertEquals("WAW-PP-001", pickupPoint.snapshot().code().value());
        assertEquals("Point by the station", pickupPoint.snapshot().name());
        assertEquals(CREATED_AT, pickupPoint.snapshot().createdAt());
    }

    @Test
    void shouldRejectCreateWhenConfigurationIsIncomplete() {
        assertThrows(
                InvalidPickupPointConfigurationException.class,
                () -> PickupPoint.create(
                        pickupPointId(),
                        new PickupPointCode("WAW-PP-001"),
                        "Point by the station",
                        PickupPointType.SERVICE_POINT,
                        Set.of(PickupPointCapability.DROP_OFF),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        CREATED_AT));
    }

    @Test
    void shouldSuspendAndResumePickupPoint() {
        final PickupPoint pickupPoint = activePickupPoint();
        final Instant suspendedAt = Instant.parse("2026-09-09T10:00:00Z");
        final Instant resumedAt = Instant.parse("2026-09-09T11:00:00Z");

        pickupPoint.suspend("Technical maintenance", suspendedAt);
        pickupPoint.resume(true, resumedAt);

        assertEquals(PickupPointStatus.ACTIVE, pickupPoint.snapshot().status());
        assertNull(pickupPoint.snapshot().statusReason());
        assertEquals(resumedAt, pickupPoint.snapshot().updatedAt());
    }

    @Test
    void shouldRejectResumeWhenDepartmentIsInactive() {
        final PickupPoint pickupPoint = activePickupPoint();
        pickupPoint.suspend("Technical maintenance", UPDATED_AT);

        assertThrows(
                InvalidPickupPointConfigurationException.class,
                () -> pickupPoint.resume(false, UPDATED_AT.plusSeconds(60)));

        assertEquals(PickupPointStatus.SUSPENDED, pickupPoint.snapshot().status());
    }

    @Test
    void shouldAllowLocationChange() {
        final PickupPoint pickupPoint = activePickupPoint();
        final PickupPointAddress changedAddress = new PickupPointAddress(
                CountryCode.PL, "00-002", "Warsaw", "Changed", "11", null);

        pickupPoint.updateDetails(
                "Point by the station",
                changedAddress,
                new GeoCoordinates(52.2297, 21.0122),
                new DepartmentId(10L),
                null,
                null,
                null,
                UPDATED_AT);

        assertEquals(changedAddress, pickupPoint.snapshot().address());
    }

    @Test
    void shouldClosePickupPointPermanently() {
        final PickupPoint pickupPoint = activePickupPoint();

        pickupPoint.close("Location closed", UPDATED_AT);

        assertEquals(PickupPointStatus.CLOSED, pickupPoint.snapshot().status());
        assertThrows(
                InvalidPickupPointConfigurationException.class,
                () -> pickupPoint.changeCapabilities(Set.of(PickupPointCapability.COLLECTION), UPDATED_AT));
    }

    @Test
    void shouldAcceptEligibleShipment() {
        final PickupPoint pickupPoint = activePickupPoint();
        final PickupPointSelectionCriteria criteria = new PickupPointSelectionCriteria(
                PickupPointCapability.DROP_OFF,
                PickupPointType.SERVICE_POINT,
                CountryCode.PL,
                PickupPointShipmentSize.SMALL,
                false);

        pickupPoint.validateSelection(criteria, true);
    }

    @Test
    void shouldRejectDangerousGoodsWhenPolicyDoesNotAllowThem() {
        final PickupPoint pickupPoint = activePickupPoint();
        final PickupPointSelectionCriteria criteria = new PickupPointSelectionCriteria(
                PickupPointCapability.DROP_OFF,
                PickupPointType.SERVICE_POINT,
                CountryCode.PL,
                PickupPointShipmentSize.SMALL,
                true);

        assertThrows(PickupPointUnavailableException.class, () -> pickupPoint.validateSelection(criteria, true));
    }

    private static PickupPoint activePickupPoint() {
        return PickupPoint.create(
                pickupPointId(),
                new PickupPointCode(" waw-pp-001 "),
                " Point by the station ",
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

    private static PickupPointId pickupPointId() {
        return new PickupPointId(UUID.fromString("9a7f3b38-73d0-4e9b-91b7-dadfeab4882f"));
    }
}
