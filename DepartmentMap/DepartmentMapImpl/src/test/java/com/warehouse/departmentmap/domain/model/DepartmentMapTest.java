package com.warehouse.departmentmap.domain.model;

import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.departmentmap.domain.enumeration.MapElementType;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.vo.ElementGeometry;
import com.warehouse.departmentmap.domain.vo.StorageLocationDefinition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.warehouse.departmentmap.DepartmentMapFixture.DEPARTMENT_ID;
import static com.warehouse.departmentmap.DepartmentMapFixture.NOW;
import static com.warehouse.departmentmap.DepartmentMapFixture.OPERATOR_ID;
import static com.warehouse.departmentmap.DepartmentMapFixture.emptyMap;
import static com.warehouse.departmentmap.DepartmentMapFixture.persistedMap;
import static com.warehouse.departmentmap.DepartmentMapFixture.rack;
import static com.warehouse.departmentmap.DepartmentMapFixture.storageLocation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DepartmentMapTest {

    @Test
    void shouldCreateEmptyMapForDepartment() {
        final DepartmentMap departmentMap = DepartmentMap.create(OPERATOR_ID, DEPARTMENT_ID, "  Hall A  ", 40, 20,
                0.5, NOW);

        assertEquals("Hall A", departmentMap.getName());
        assertEquals(DEPARTMENT_ID, departmentMap.getDepartmentId());
        assertEquals(0, departmentMap.getElements().size());
        assertNull(departmentMap.getVersion());
    }

    @Test
    void shouldRejectMapWithInvalidDimensions() {
        assertThrows(DepartmentMapException.class,
                () -> DepartmentMap.create(OPERATOR_ID, DEPARTMENT_ID, "Hall", 0, 20, 1, NOW));
        assertThrows(DepartmentMapException.class,
                () -> DepartmentMap.create(OPERATOR_ID, DEPARTMENT_ID, "Hall", 20, 20, 0, NOW));
        assertThrows(DepartmentMapException.class,
                () -> DepartmentMap.create(OPERATOR_ID, DEPARTMENT_ID, " ", 20, 20, 1, NOW));
        assertThrows(DepartmentMapException.class,
                () -> DepartmentMap.create(OPERATOR_ID, null, "Hall", 20, 20, 1, NOW));
    }

    @Test
    void shouldReplaceLayoutAndExposeStorageLocations() {
        final DepartmentMap departmentMap = emptyMap();

        departmentMap.changeLayout("Hall A", 50, 30, 1,
                List.of(rack("Rack 1"), storageLocation("a-01", 1), storageLocation("A-02", 2)), NOW);

        final List<StorageLocationDefinition> definitions = departmentMap.storageLocationDefinitions();
        assertEquals(3, departmentMap.getElements().size());
        assertEquals(2, definitions.size());
        assertEquals("A-01", definitions.get(0).code());
        assertEquals(2, definitions.get(1).capacity());
    }

    @Test
    void shouldRejectDuplicatedLocationCodes() {
        final DepartmentMap departmentMap = emptyMap();

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> departmentMap.changeLayout("Hall A", 50, 30, 1,
                        List.of(storageLocation("A-01", 1), storageLocation("a-01", 1)), NOW));

        assertEquals(400, exception.getStatus());
    }

    @Test
    void shouldRejectDuplicatedElementIds() {
        final DepartmentMap departmentMap = emptyMap();
        final MapElementId elementId = MapElementId.generate();

        assertThrows(DepartmentMapException.class, () -> departmentMap.changeLayout("Hall A", 50, 30, 1,
                List.of(storageLocation(elementId, "A-01", 1), storageLocation(elementId, "A-02", 1)), NOW));
    }

    @Test
    void shouldRejectElementOutsideMap() {
        final DepartmentMap departmentMap = emptyMap();
        final MapElement outside = new MapElement(MapElementId.generate(), MapElementType.ZONE, "Yard",
                new ElementGeometry(60, 5, 4, 4, 0), null, null, null);

        assertThrows(DepartmentMapException.class,
                () -> departmentMap.changeLayout("Hall A", 50, 30, 1, List.of(outside), NOW));
    }

    @Test
    void shouldThrowConflictWhenVersionDoesNotMatch() {
        final DepartmentMap departmentMap = persistedMap(List.of());

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> departmentMap.verifyVersion(3L));

        assertEquals("MAP_VERSION_CONFLICT", exception.getCode());
        assertEquals(409, exception.getStatus());
    }

    @Test
    void shouldValidateStorageLocationElement() {
        assertThrows(DepartmentMapException.class, () -> storageLocation(" ", 1));
        assertThrows(DepartmentMapException.class, () -> storageLocation("A 01", 1));
        assertThrows(DepartmentMapException.class, () -> storageLocation("A-01", 0));
        assertThrows(DepartmentMapException.class, () -> storageLocation("A-01", MapElement.MAX_CAPACITY + 1));
    }

    @Test
    void shouldNormalizeRotationAndIgnoreLocationFieldsOnNonStorageElements() {
        final MapElement wall = new MapElement(MapElementId.generate(), MapElementType.WALL, "Wall",
                new ElementGeometry(1, 1, 10, 0.2, -90), "#aabbcc", "IGNORED", 5);

        assertEquals(270, wall.getGeometry().rotation());
        assertNull(wall.getLocationCode());
        assertNull(wall.getCapacity());
        assertEquals("#AABBCC", wall.getColor());
    }

    @Test
    void shouldAcceptSorterBeltAsLayoutElement() {
        final DepartmentMap departmentMap = emptyMap();
        final MapElement sorter = new MapElement(MapElementId.generate(), MapElementType.SORTER, "Sorter 1",
                new ElementGeometry(5, 20, 30, 1.2, 180), "#4B5563", "IGNORED", 3);

        departmentMap.changeLayout("Hall A", 50, 30, 1, List.of(sorter, storageLocation("A-01", 1)), NOW);

        assertEquals(MapElementType.SORTER, departmentMap.getElements().get(0).getType());
        assertNull(departmentMap.getElements().get(0).getLocationCode());
        assertEquals(1, departmentMap.storageLocationDefinitions().size());
    }
}
