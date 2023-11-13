package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import lombok.Data;

@Data
public class ReturnPackageRequestDto {
    private ParcelDto parcel;
    private String reason;
    private ReturnStatusDto returnStatus;
    private String returnToken;
    private SupplierCodeDto supplierCode;
}
