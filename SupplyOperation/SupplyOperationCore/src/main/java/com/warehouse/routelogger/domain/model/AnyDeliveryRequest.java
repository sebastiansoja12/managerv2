package com.warehouse.routelogger.domain.model;


import com.warehouse.routelogger.domain.enumeration.ProcessType;

import lombok.Data;

@Data
public class AnyDeliveryRequest {
    private String depotCode;
    private String supplierCode;
    private Long parcelId;
    private ProcessType processType;
}
