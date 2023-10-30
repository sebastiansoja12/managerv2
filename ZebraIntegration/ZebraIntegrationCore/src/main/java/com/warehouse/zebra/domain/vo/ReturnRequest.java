package com.warehouse.zebra.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReturnRequest {

    private Parcel parcel;

    private String reason;

    private ReturnStatus returnStatus;

    private String returnToken;

    private String supplierCode;
}
