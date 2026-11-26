package com.warehouse.analytics.infrastructure.adapter.primary.api;

import java.math.BigDecimal;
import java.util.List;

public record DepartmentForecastApiResponse(String departmentCode,
                                            String city,
                                            BigDecimal averageShipments,
                                            Long expectedShipments,
                                            Long minimumObserved,
                                            Long maximumObserved,
                                            Long actualShipments,
                                            List<DailyObservationApiResponse> observations) {
}
