package com.warehouse.organisationstructure.deliveryarea.application.port.secondary;

import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaPolicies;

public interface DeliveryAreaConfigurationServicePort {

    DeliveryAreaPolicies currentPolicies();
}
