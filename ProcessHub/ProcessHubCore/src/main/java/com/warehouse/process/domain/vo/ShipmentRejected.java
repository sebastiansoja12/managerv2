package com.warehouse.process.domain.vo;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;

public record ShipmentRejected(ServiceType serviceType,
                               ProcessType processType,
                               String request,
                               String response,
                               String faultDescription) {
}
