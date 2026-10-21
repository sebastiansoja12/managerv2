package com.warehouse.departmentmap.domain.vo;

import com.warehouse.commonassets.identificator.MapElementId;

public record StorageLocationDefinition(MapElementId locationId, String code, int capacity) {
}
