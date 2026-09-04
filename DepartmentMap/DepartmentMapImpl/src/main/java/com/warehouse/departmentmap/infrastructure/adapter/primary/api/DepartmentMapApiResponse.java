package com.warehouse.departmentmap.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DepartmentMapApiResponse(UUID mapId,
                                       Long departmentId,
                                       String name,
                                       double width,
                                       double height,
                                       double gridSize,
                                       Long version,
                                       Instant createdAt,
                                       Instant updatedAt,
                                       List<MapElementApi> elements,
                                       List<StorageLocationApiResponse> locations) {
}
