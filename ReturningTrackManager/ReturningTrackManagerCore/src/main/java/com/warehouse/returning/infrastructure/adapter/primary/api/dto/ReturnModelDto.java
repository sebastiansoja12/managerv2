package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;


@Value
@Builder
@Jacksonized
public class ReturnModelDto {
    ParcelIdDto parcelId;
    ReasonDto reason;
    ReturnStatusDto returnStatus;
    ReturnTokenDto returnToken;
    SupplierCodeDto supplierCode;
    DepotCodeDto depotCode;
    UsernameDto username;
}
