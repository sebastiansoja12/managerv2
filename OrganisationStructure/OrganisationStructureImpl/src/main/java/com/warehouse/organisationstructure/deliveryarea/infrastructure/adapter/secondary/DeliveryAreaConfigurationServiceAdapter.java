package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary;

import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.DeliveryAreaConfigurationServicePort;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;

public class DeliveryAreaConfigurationServiceAdapter implements DeliveryAreaConfigurationServicePort {

    private final OperatorConfigurationPort operatorConfigurationPort;

    public DeliveryAreaConfigurationServiceAdapter(final OperatorConfigurationPort operatorConfigurationPort) {
        this.operatorConfigurationPort = operatorConfigurationPort;
    }

    @Override
    public boolean isAutomaticAssignmentEnabled() {
        return operatorConfigurationPort.getCurrentDepartmentConfiguration().isAutoAssignDeliveryAreas();
    }
}
