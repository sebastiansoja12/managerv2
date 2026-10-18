package com.warehouse.departmentmap.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.util.UUID;

public record DepartmentMapSummaryApiResponse(UUID mapId,
                                              Long departmentId,
                                              String name,
                                              double width,
                                              double height,
                                              Long version,
                                              Instant updatedAt,
                                              int locationCount,
                                              int occupiedLocationCount,
                                              int palletCount) {
}
