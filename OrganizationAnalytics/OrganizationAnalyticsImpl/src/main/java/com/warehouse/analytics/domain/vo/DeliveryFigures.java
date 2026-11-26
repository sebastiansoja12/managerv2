package com.warehouse.analytics.domain.vo;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentId;

import java.math.BigDecimal;

public record DeliveryFigures(DepartmentId originDepartmentId,
                              Currency currency,
                              long deliveredShipments,
                              BigDecimal deliveredValue) {
}
