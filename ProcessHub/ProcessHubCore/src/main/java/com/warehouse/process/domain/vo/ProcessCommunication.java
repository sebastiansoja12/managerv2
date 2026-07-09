package com.warehouse.process.domain.vo;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;

public record ProcessCommunication(
        ServiceType sourceServiceType,
        ServiceType targetServiceType,
        ProcessType processType,
        String request,
        String response,
        String faultDescription) {
}
