package com.warehouse.deliverynetwork.application.port.secondary;

import com.warehouse.deliverynetwork.domain.vo.DepartmentConnectionPolicies;

public interface DeliveryNetworkConfigurationServicePort {

    DepartmentConnectionPolicies currentPolicies();
}
