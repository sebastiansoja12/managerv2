package com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto;

import lombok.Value;

@Value
public class SupplierCodeRequestDto {
    String supplierCode;
    String processType;
    Long parcelId;
}
