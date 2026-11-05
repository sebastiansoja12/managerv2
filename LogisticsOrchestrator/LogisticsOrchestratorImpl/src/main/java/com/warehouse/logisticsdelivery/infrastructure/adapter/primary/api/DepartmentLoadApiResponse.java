package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api;

public record DepartmentLoadApiResponse(Long departmentId, long deliveriesToday, int dailyCapacity,
                                        int utilizationPercent, boolean warning, boolean overloaded) {
}
