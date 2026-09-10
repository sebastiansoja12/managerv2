package com.warehouse.departmentmap.infrastructure.adapter.primary.api;

import java.util.List;

public record SaveDepartmentMapLayoutApiRequest(String name,
                                                Double width,
                                                Double height,
                                                Double gridSize,
                                                Long version,
                                                List<MapElementApi> elements) {
}
