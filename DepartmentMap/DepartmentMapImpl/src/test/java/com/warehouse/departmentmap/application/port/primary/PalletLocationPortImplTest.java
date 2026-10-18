package com.warehouse.departmentmap.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.departmentmap.InMemoryDepartmentMapStore;
import com.warehouse.departmentmap.application.port.primary.command.CreateDepartmentMapCommand;
import com.warehouse.departmentmap.application.port.primary.command.ReserveLocationCommand;
import com.warehouse.departmentmap.application.port.primary.command.SaveDepartmentMapLayoutCommand;
import com.warehouse.departmentmap.application.port.primary.result.PalletLocationResult;
import com.warehouse.departmentmap.domain.enumeration.LocationStatus;
import com.warehouse.departmentmap.domain.enumeration.PalletMovementType;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.model.PalletMovement;
import com.warehouse.departmentmap.domain.service.StorageLocationSynchronizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static com.warehouse.departmentmap.DepartmentMapFixture.DEPARTMENT_ID;
import static com.warehouse.departmentmap.DepartmentMapFixture.OPERATOR_ID;
import static com.warehouse.departmentmap.DepartmentMapFixture.USER_ID;
import static com.warehouse.departmentmap.DepartmentMapFixture.pallet;
import static com.warehouse.departmentmap.DepartmentMapFixture.storageLocation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PalletLocationPortImplTest {

    private final MapElementId firstLocationId = MapElementId.generate();
    private final MapElementId secondLocationId = MapElementId.generate();
    private InMemoryDepartmentMapStore store;
    private PalletLocationPortImpl palletLocationPort;
    private DepartmentMapId mapId;

    @BeforeEach
    void setUp() {
        this.store = new InMemoryDepartmentMapStore(Set.of(DEPARTMENT_ID), OPERATOR_ID, USER_ID);
        final DepartmentMapPortImpl departmentMapPort = new DepartmentMapPortImpl(store, store, store, store,
                new StorageLocationSynchronizer());
        this.palletLocationPort = new PalletLocationPortImpl(store, store, store, store);
        this.mapId = departmentMapPort.createMap(new CreateDepartmentMapCommand(DEPARTMENT_ID, "Hall A", 50, 30, 1))
                .departmentMap().getDepartmentMapId();
        departmentMapPort.saveLayout(mapId, new SaveDepartmentMapLayoutCommand("Hall A", 50, 30, 1, 0L, List.of(
                storageLocation(firstLocationId, "A-01", 1), storageLocation(secondLocationId, "A-02", 1))));
    }

    @Test
    void shouldPlacePalletAndRecordHistory() {
        final PalletLocationResult result = palletLocationPort.placePallet(firstLocationId, pallet("pal000000000001"));

        assertEquals("A-01", result.location().getCode());
        assertEquals(mapId, result.departmentMap().getDepartmentMapId());
        final List<PalletMovement> history = palletLocationPort.palletHistory(pallet("PAL000000000001"));
        assertEquals(1, history.size());
        assertEquals(PalletMovementType.PLACED, history.get(0).movementType());
        assertEquals(USER_ID, history.get(0).userId());
    }

    @Test
    void shouldBlockSecondPalletOnSinglePalletLocation() {
        palletLocationPort.placePallet(firstLocationId, pallet("PAL1"));

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> palletLocationPort.placePallet(firstLocationId, pallet("PAL2")));

        assertEquals("LOCATION_OCCUPIED", exception.getCode());
        assertTrue(palletLocationPort.findPalletLocation(pallet("PAL2")).isEmpty());
    }

    @Test
    void shouldRejectPlacingPalletThatAlreadyStandsElsewhere() {
        palletLocationPort.placePallet(firstLocationId, pallet("PAL1"));

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> palletLocationPort.placePallet(secondLocationId, pallet("PAL1")));

        assertEquals("PALLET_ALREADY_PLACED", exception.getCode());
    }

    @Test
    void shouldMovePalletBetweenLocations() {
        palletLocationPort.placePallet(firstLocationId, pallet("PAL1"));

        palletLocationPort.movePallet(pallet("PAL1"), secondLocationId);

        assertEquals("A-02", palletLocationPort.findPalletLocation(pallet("PAL1")).orElseThrow().location().getCode());
        assertEquals(LocationStatus.FREE, store.findById(firstLocationId).orElseThrow().status());
        final PalletMovement movement = palletLocationPort.palletHistory(pallet("PAL1")).get(0);
        assertEquals(PalletMovementType.MOVED, movement.movementType());
        assertEquals("A-01", movement.fromLocation().code());
        assertEquals("A-02", movement.toLocation().code());
    }

    @Test
    void shouldKeepPalletOnSourceWhenTargetIsOccupied() {
        palletLocationPort.placePallet(firstLocationId, pallet("PAL1"));
        palletLocationPort.placePallet(secondLocationId, pallet("PAL2"));

        assertThrows(DepartmentMapException.class, () -> palletLocationPort.movePallet(pallet("PAL1"),
                secondLocationId));

        assertEquals("A-01", palletLocationPort.findPalletLocation(pallet("PAL1")).orElseThrow().location().getCode());
    }

    @Test
    void shouldRejectMoveToSameLocation() {
        palletLocationPort.placePallet(firstLocationId, pallet("PAL1"));

        assertThrows(DepartmentMapException.class, () -> palletLocationPort.movePallet(pallet("PAL1"),
                firstLocationId));
    }

    @Test
    void shouldReserveLocationForPalletAndBlockOthers() {
        palletLocationPort.reserveLocation(firstLocationId, new ReserveLocationCommand(pallet("PAL9"), "Truck 12"));

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> palletLocationPort.placePallet(firstLocationId, pallet("PAL1")));
        palletLocationPort.placePallet(firstLocationId, pallet("PAL9"));

        assertEquals("LOCATION_RESERVED", exception.getCode());
        assertEquals(LocationStatus.OCCUPIED, store.findById(firstLocationId).orElseThrow().status());
        assertEquals(List.of(PalletMovementType.PLACED, PalletMovementType.RESERVED),
                palletLocationPort.palletHistory(pallet("PAL9")).stream().map(PalletMovement::movementType).toList());
    }

    @Test
    void shouldCancelReservation() {
        palletLocationPort.reserveLocation(firstLocationId, new ReserveLocationCommand(null, null));

        palletLocationPort.cancelReservation(firstLocationId);

        assertEquals(LocationStatus.FREE, store.findById(firstLocationId).orElseThrow().status());
    }

    @Test
    void shouldFindPalletsByFragment() {
        palletLocationPort.placePallet(firstLocationId, pallet("PAL000000000011"));
        palletLocationPort.placePallet(secondLocationId, pallet("PAL000000000012"));

        final List<PalletLocationResult> results = palletLocationPort.searchPallets("0000011");

        assertEquals(1, results.size());
        assertEquals("A-01", results.get(0).location().getCode());
        assertThrows(DepartmentMapException.class, () -> palletLocationPort.searchPallets("P"));
    }

    @Test
    void shouldRemovePalletAndRecordHistory() {
        palletLocationPort.placePallet(firstLocationId, pallet("PAL1"));

        palletLocationPort.removePallet(pallet("PAL1"));

        assertTrue(palletLocationPort.findPalletLocation(pallet("PAL1")).isEmpty());
        assertEquals(PalletMovementType.REMOVED, palletLocationPort.palletHistory(pallet("PAL1")).get(0).movementType());
        assertThrows(DepartmentMapException.class, () -> palletLocationPort.removePallet(pallet("PAL1")));
    }
}
