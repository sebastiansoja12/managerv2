package com.warehouse.departmentmap;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.departmentmap.domain.enumeration.MapElementType;
import com.warehouse.departmentmap.domain.model.DepartmentMap;
import com.warehouse.departmentmap.domain.model.MapElement;
import com.warehouse.departmentmap.domain.model.StorageLocation;
import com.warehouse.departmentmap.domain.vo.ElementGeometry;
import com.warehouse.departmentmap.domain.vo.StorageLocationDefinition;

import java.time.Instant;
import java.util.List;

public final class DepartmentMapFixture {

    public static final OperatorId OPERATOR_ID = OperatorId.of(10L);
    public static final UserId USER_ID = new UserId(7L);
    public static final DepartmentId DEPARTMENT_ID = new DepartmentId(100L);
    public static final Instant NOW = Instant.parse("2026-10-18T10:00:00Z");

    private DepartmentMapFixture() {
    }

    public static DepartmentMap emptyMap() {
        return DepartmentMap.create(OPERATOR_ID, DEPARTMENT_ID, "Hall A", 50, 30, 1, NOW);
    }

    public static DepartmentMap persistedMap(final List<MapElement> elements) {
        return new DepartmentMap(DepartmentMapId.generate(), OPERATOR_ID, DEPARTMENT_ID, "Hall A", 50, 30, 1,
                elements, 0L, NOW, NOW);
    }

    public static MapElement storageLocation(final String code, final int capacity) {
        return storageLocation(MapElementId.generate(), code, capacity);
    }

    public static MapElement storageLocation(final MapElementId elementId, final String code, final int capacity) {
        return new MapElement(elementId, MapElementType.STORAGE_LOCATION, null,
                new ElementGeometry(2, 2, 1.2, 0.8, 0), null, code, capacity);
    }

    public static MapElement rack(final String label) {
        return new MapElement(MapElementId.generate(), MapElementType.RACK, label,
                new ElementGeometry(5, 5, 10, 1, 90), "#3366CC", null, null);
    }

    public static StorageLocation location(final String code, final int capacity) {
        return StorageLocation.define(new StorageLocationDefinition(MapElementId.generate(), code, capacity),
                DepartmentMapId.generate(), OPERATOR_ID);
    }

    public static StorageLocation location(final DepartmentMapId departmentMapId, final String code,
                                           final int capacity) {
        return StorageLocation.define(new StorageLocationDefinition(MapElementId.generate(), code, capacity),
                departmentMapId, OPERATOR_ID);
    }

    public static PalletTrackingNumber pallet(final String value) {
        return new PalletTrackingNumber(value);
    }
}
