package com.warehouse.departmentmap.infrastructure.adapter.primary.api;

public record CreateDepartmentMapApiRequest(Long departmentId,
                                            String name,
                                            Double width,
                                            Double height,
                                            Double gridSize) {
}
