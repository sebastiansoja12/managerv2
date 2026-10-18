package com.warehouse.departmentmap.domain.model;

import com.warehouse.departmentmap.domain.enumeration.LocationStatus;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.vo.StorageLocationDefinition;
import org.junit.jupiter.api.Test;

import static com.warehouse.departmentmap.DepartmentMapFixture.NOW;
import static com.warehouse.departmentmap.DepartmentMapFixture.USER_ID;
import static com.warehouse.departmentmap.DepartmentMapFixture.location;
import static com.warehouse.departmentmap.DepartmentMapFixture.pallet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageLocationTest {

    @Test
    void shouldPlacePalletOnFreeLocation() {
        final StorageLocation storageLocation = location("A-01", 1);

        storageLocation.place(pallet("PAL000000000001"), USER_ID, NOW);

        assertTrue(storageLocation.holds(pallet("pal000000000001")));
        assertEquals(LocationStatus.OCCUPIED, storageLocation.status());
    }

    @Test
    void shouldRejectSecondPalletWhenLocationHoldsSinglePallet() {
        final StorageLocation storageLocation = location("A-01", 1);
        storageLocation.place(pallet("PAL000000000001"), USER_ID, NOW);

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> storageLocation.place(pallet("PAL000000000002"), USER_ID, NOW));

        assertEquals("LOCATION_OCCUPIED", exception.getCode());
        assertEquals(409, exception.getStatus());
        assertEquals(1, storageLocation.getPallets().size());
    }

    @Test
    void shouldAcceptPalletsUpToCapacityAndReportPartialOccupancy() {
        final StorageLocation storageLocation = location("B-01", 3);

        storageLocation.place(pallet("PAL1"), USER_ID, NOW);
        storageLocation.place(pallet("PAL2"), USER_ID, NOW);

        assertEquals(LocationStatus.PARTIALLY_OCCUPIED, storageLocation.status());
        storageLocation.place(pallet("PAL3"), USER_ID, NOW);
        assertEquals(LocationStatus.OCCUPIED, storageLocation.status());
        assertThrows(DepartmentMapException.class, () -> storageLocation.place(pallet("PAL4"), USER_ID, NOW));
    }

    @Test
    void shouldRejectSamePalletTwiceOnLocation() {
        final StorageLocation storageLocation = location("B-01", 3);
        storageLocation.place(pallet("PAL1"), USER_ID, NOW);

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> storageLocation.place(pallet("PAL1"), USER_ID, NOW));

        assertEquals("PALLET_ALREADY_PLACED", exception.getCode());
    }

    @Test
    void shouldBlockOtherPalletWhenLocationIsReservedForSpecificPallet() {
        final StorageLocation storageLocation = location("A-01", 1);
        storageLocation.reserve(pallet("PAL1"), "Inbound truck", USER_ID, NOW);

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> storageLocation.place(pallet("PAL2"), USER_ID, NOW));

        assertEquals("LOCATION_RESERVED", exception.getCode());
        assertEquals(LocationStatus.RESERVED, storageLocation.status());
    }

    @Test
    void shouldConsumeReservationWhenReservedPalletArrives() {
        final StorageLocation storageLocation = location("A-01", 1);
        storageLocation.reserve(pallet("PAL1"), null, USER_ID, NOW);

        storageLocation.place(pallet("PAL1"), USER_ID, NOW);

        assertTrue(storageLocation.getReservation().isEmpty());
        assertEquals(LocationStatus.OCCUPIED, storageLocation.status());
    }

    @Test
    void shouldBlockAnyPalletWhenGenericReservationTakesLastSlot() {
        final StorageLocation storageLocation = location("A-01", 1);
        storageLocation.reserve(null, "Keep free", USER_ID, NOW);

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> storageLocation.place(pallet("PAL2"), USER_ID, NOW));

        assertEquals("LOCATION_RESERVED", exception.getCode());
    }

    @Test
    void shouldAllowPalletOnReservedLocationWhenAnotherSlotIsFree() {
        final StorageLocation storageLocation = location("B-01", 2);
        storageLocation.reserve(pallet("PAL1"), null, USER_ID, NOW);

        storageLocation.place(pallet("PAL2"), USER_ID, NOW);

        assertTrue(storageLocation.getReservation().isPresent());
        assertEquals(LocationStatus.RESERVED, storageLocation.status());
    }

    @Test
    void shouldRejectReservationOfFullLocation() {
        final StorageLocation storageLocation = location("A-01", 1);
        storageLocation.place(pallet("PAL1"), USER_ID, NOW);

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> storageLocation.reserve(null, null, USER_ID, NOW));

        assertEquals("LOCATION_OCCUPIED", exception.getCode());
    }

    @Test
    void shouldRejectSecondReservation() {
        final StorageLocation storageLocation = location("A-01", 1);
        storageLocation.reserve(null, null, USER_ID, NOW);

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> storageLocation.reserve(pallet("PAL1"), null, USER_ID, NOW));

        assertEquals("LOCATION_ALREADY_RESERVED", exception.getCode());
    }

    @Test
    void shouldCancelReservation() {
        final StorageLocation storageLocation = location("A-01", 1);
        storageLocation.reserve(pallet("PAL1"), null, USER_ID, NOW);

        storageLocation.cancelReservation();

        assertEquals(LocationStatus.FREE, storageLocation.status());
        assertThrows(DepartmentMapException.class, storageLocation::cancelReservation);
    }

    @Test
    void shouldRemovePallet() {
        final StorageLocation storageLocation = location("A-01", 1);
        storageLocation.place(pallet("PAL1"), USER_ID, NOW);

        storageLocation.remove(pallet("PAL1"));

        assertTrue(storageLocation.isEmpty());
        assertEquals(LocationStatus.FREE, storageLocation.status());
    }

    @Test
    void shouldThrowExceptionWhenRemovingPalletThatIsNotOnLocation() {
        final StorageLocation storageLocation = location("A-01", 1);

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> storageLocation.remove(pallet("PAL1")));

        assertEquals(404, exception.getStatus());
    }

    @Test
    void shouldRejectCapacityBelowCurrentOccupancy() {
        final StorageLocation storageLocation = location("B-01", 3);
        storageLocation.place(pallet("PAL1"), USER_ID, NOW);
        storageLocation.place(pallet("PAL2"), USER_ID, NOW);

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> storageLocation.redefine(new StorageLocationDefinition(storageLocation.getLocationId(),
                        "B-01", 1)));

        assertEquals("CAPACITY_BELOW_OCCUPANCY", exception.getCode());
        assertEquals(3, storageLocation.getCapacity());
    }

    @Test
    void shouldRenameLocationAndKeepPallets() {
        final StorageLocation storageLocation = location("B-01", 2);
        storageLocation.place(pallet("PAL1"), USER_ID, NOW);

        storageLocation.redefine(new StorageLocationDefinition(storageLocation.getLocationId(), "C-01", 2));

        assertEquals("C-01", storageLocation.getCode());
        assertFalse(storageLocation.isEmpty());
    }
}
