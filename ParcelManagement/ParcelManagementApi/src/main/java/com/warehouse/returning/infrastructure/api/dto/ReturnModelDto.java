package com.warehouse.returning.infrastructure.api.dto;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class ReturnModelDto {
    ParcelIdDto parcelId;
    ReasonDto reason;
    ReturnStatusDto returnStatus;
    ReturnTokenDto returnToken;
    SupplierCodeDto supplierCode;
    DepotCodeDto depotCode;
    UsernameDto username;
}
