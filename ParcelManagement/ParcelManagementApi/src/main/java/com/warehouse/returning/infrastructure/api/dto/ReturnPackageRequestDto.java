package com.warehouse.returning.infrastructure.api.dto;

import lombok.Data;

@Data
public class ReturnPackageRequestDto {
    private ParcelDto parcel;
    private String reason;
    private ReturnStatusDto returnStatus;
    private String returnToken;
    private String supplierCode;
}
