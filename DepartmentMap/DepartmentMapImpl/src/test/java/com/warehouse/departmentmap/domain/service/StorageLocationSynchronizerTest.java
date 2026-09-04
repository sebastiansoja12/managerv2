package com.warehouse.departmentmap.domain.service;

import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.model.DepartmentMap;
import com.warehouse.departmentmap.domain.model.StorageLocation;
import com.warehouse.departmentmap.domain.vo.Plan;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.warehouse.departmentmap.DepartmentMapFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StorageLocationSynchronizerTest {

    private final StorageLocationSynchronizer synchronizer = new StorageLocationSynchronizer();

    @Test
    void shouldCreateChangeAndRemoveLocationsAccordingToLayout() {
        final StorageLocation unchanged = location("A-01", 1);
        final StorageLocation renamed = location("A-02", 1);
        final StorageLocation removed = location("A-03", 1);
        final MapElementId newLocationId = MapElementId.generate();
        final DepartmentMap departmentMap = persistedMap(List.of(
                storageLocation(unchanged.getLocationId(), "A-01", 1),
                storageLocation(renamed.getLocationId(), "B-02", 2),
                storageLocation(newLocationId, "A-04", 1)));

        final Plan plan = synchronizer.plan(departmentMap,
                List.of(unchanged, renamed, removed));

        assertEquals(1, plan.created().size());
        assertEquals(newLocationId, plan.created().getFirst().getLocationId());
        assertEquals(departmentMap.getDepartmentMapId(), plan.created().getFirst().getDepartmentMapId());
        assertEquals(List.of(renamed), plan.changed());
        assertEquals("B-02", renamed.getCode());
        assertEquals(2, renamed.getCapacity());
        assertEquals(List.of(removed), plan.removed());
    }

    @Test
    void shouldRejectRemovingLocationThatHoldsPallets() {
        final StorageLocation occupied = location("A-01", 1);
        occupied.place(pallet("PAL1"), USER_ID, NOW);
        final DepartmentMap departmentMap = persistedMap(List.of());

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> synchronizer.plan(departmentMap, List.of(occupied)));

        assertEquals("LOCATION_NOT_EMPTY", exception.getCode());
    }
}
