package com.warehouse.departmentmap.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.departmentmap.InMemoryDepartmentMapStore;
import com.warehouse.departmentmap.application.port.primary.command.CreateDepartmentMapCommand;
import com.warehouse.departmentmap.application.port.primary.command.SaveDepartmentMapLayoutCommand;
import com.warehouse.departmentmap.application.port.primary.result.DepartmentMapSummary;
import com.warehouse.departmentmap.application.port.primary.result.DepartmentMapView;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.service.StorageLocationSynchronizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static com.warehouse.departmentmap.DepartmentMapFixture.DEPARTMENT_ID;
import static com.warehouse.departmentmap.DepartmentMapFixture.OPERATOR_ID;
import static com.warehouse.departmentmap.DepartmentMapFixture.USER_ID;
import static com.warehouse.departmentmap.DepartmentMapFixture.pallet;
import static com.warehouse.departmentmap.DepartmentMapFixture.rack;
import static com.warehouse.departmentmap.DepartmentMapFixture.storageLocation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DepartmentMapPortImplTest {

    private InMemoryDepartmentMapStore store;
    private DepartmentMapPortImpl departmentMapPort;
    private PalletLocationPortImpl palletLocationPort;

    @BeforeEach
    void setUp() {
        this.store = new InMemoryDepartmentMapStore(Set.of(DEPARTMENT_ID), OPERATOR_ID, USER_ID);
        this.departmentMapPort = new DepartmentMapPortImpl(store, store, store, store,
                new StorageLocationSynchronizer());
        this.palletLocationPort = new PalletLocationPortImpl(store, store, store, store);
    }

    @Test
    void shouldCreateMapForExistingDepartment() {
        final DepartmentMapView view = departmentMapPort.createMap(
                new CreateDepartmentMapCommand(DEPARTMENT_ID, "Hall A", 40, 25, 1));

        assertEquals(0L, view.departmentMap().getVersion());
        assertEquals(OPERATOR_ID, view.departmentMap().getOperatorId());
        assertEquals(1, departmentMapPort.findMaps(DEPARTMENT_ID).size());
    }

    @Test
    void shouldThrowExceptionWhenDepartmentDoesNotExist() {
        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> departmentMapPort.createMap(new CreateDepartmentMapCommand(new DepartmentId(999L), "Hall", 10,
                        10, 1)));

        assertEquals("DEPARTMENT_NOT_FOUND", exception.getCode());
    }

    @Test
    void shouldSaveLayoutAndCreateStorageLocations() {
        final DepartmentMapId mapId = createMap();

        final DepartmentMapView view = departmentMapPort.saveLayout(mapId, layout(0L,
                List.of(rack("R1"), storageLocation("A-01", 1), storageLocation("A-02", 2))));

        assertEquals(1L, view.departmentMap().getVersion());
        assertEquals(3, view.departmentMap().getElements().size());
        assertEquals(2, view.locations().size());
    }

    @Test
    void shouldRejectLayoutSavedWithStaleVersion() {
        final DepartmentMapId mapId = createMap();
        departmentMapPort.saveLayout(mapId, layout(0L, List.of(storageLocation("A-01", 1))));

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> departmentMapPort.saveLayout(mapId, layout(0L, List.of())));

        assertEquals("MAP_VERSION_CONFLICT", exception.getCode());
    }

    @Test
    void shouldRejectRemovingOccupiedLocationFromLayout() {
        final DepartmentMapId mapId = createMap();
        final MapElementId locationId = MapElementId.generate();
        departmentMapPort.saveLayout(mapId, layout(0L, List.of(storageLocation(locationId, "A-01", 1))));
        palletLocationPort.placePallet(locationId, pallet("PAL1"));

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> departmentMapPort.saveLayout(mapId, layout(1L, List.of())));

        assertEquals("LOCATION_NOT_EMPTY", exception.getCode());
        assertEquals(1, store.locationCount());
    }

    @Test
    void shouldReportOccupancyInSummary() {
        final DepartmentMapId mapId = createMap();
        final MapElementId locationId = MapElementId.generate();
        departmentMapPort.saveLayout(mapId, layout(0L, List.of(storageLocation(locationId, "A-01", 1),
                storageLocation("A-02", 1))));
        palletLocationPort.placePallet(locationId, pallet("PAL1"));

        final DepartmentMapSummary summary = departmentMapPort.findMaps(DEPARTMENT_ID).get(0);

        assertEquals(2, summary.locationCount());
        assertEquals(1, summary.occupiedLocationCount());
        assertEquals(1, summary.palletCount());
    }

    @Test
    void shouldDeleteEmptyMapAndRejectDeletingMapWithPallets() {
        final DepartmentMapId mapId = createMap();
        final MapElementId locationId = MapElementId.generate();
        departmentMapPort.saveLayout(mapId, layout(0L, List.of(storageLocation(locationId, "A-01", 1))));
        palletLocationPort.placePallet(locationId, pallet("PAL1"));

        assertThrows(DepartmentMapException.class, () -> departmentMapPort.deleteMap(mapId));
        palletLocationPort.removePallet(pallet("PAL1"));
        departmentMapPort.deleteMap(mapId);

        assertEquals(0, departmentMapPort.findMaps(DEPARTMENT_ID).size());
        assertEquals(0, store.locationCount());
    }

    private DepartmentMapId createMap() {
        return departmentMapPort.createMap(new CreateDepartmentMapCommand(DEPARTMENT_ID, "Hall A", 50, 30, 1))
                .departmentMap().getDepartmentMapId();
    }

    private static SaveDepartmentMapLayoutCommand layout(final Long version,
                                                         final List<com.warehouse.departmentmap.domain.model.MapElement> elements) {
        return new SaveDepartmentMapLayoutCommand("Hall A", 50, 30, 1, version, elements);
    }
}
