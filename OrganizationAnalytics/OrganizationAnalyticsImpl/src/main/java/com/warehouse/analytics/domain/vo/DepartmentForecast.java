package com.warehouse.analytics.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;

import java.math.BigDecimal;
import java.util.List;

public record DepartmentForecast(DepartmentCode departmentCode,
                                 String city,
                                 BigDecimal averageShipments,
                                 Long expectedShipments,
                                 Long minimumObserved,
                                 Long maximumObserved,
                                 Long actualShipments,
                                 List<DailyObservation> observations) {
}
