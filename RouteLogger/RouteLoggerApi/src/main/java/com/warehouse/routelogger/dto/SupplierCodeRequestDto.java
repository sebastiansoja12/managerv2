package com.warehouse.routelogger.dto;

import lombok.Value;

@Value
public class SupplierCodeRequestDto {
    String supplierCode;
    ProcessTypeDto processType;
    Long parcelId;
}
