package com.warehouse.auth.configuration.access;

import com.warehouse.auth.OperatorCapabilityService;
import org.springframework.stereotype.Component;

@Component("operatorCapabilityPolicy")
public class OperatorCapabilityPolicy {

    private final OperatorCapabilityService operatorCapabilityService;

    public OperatorCapabilityPolicy(final OperatorCapabilityService operatorCapabilityService) {
        this.operatorCapabilityService = operatorCapabilityService;
    }

    public boolean supportsReturnShipments() {
        return operatorCapabilityService.supportsReturnShipments();
    }
}
