package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary;

import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.DeliveryAreaConfigurationServicePort;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaPolicies;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaRules;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DeliveryAreaMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfigurations;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentDeliveryAreaPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;

import java.util.Map;
import java.util.stream.Collectors;

public class DeliveryAreaConfigurationServiceAdapter implements DeliveryAreaConfigurationServicePort {

    private final OperatorConfigurationPort operatorConfigurationPort;

    public DeliveryAreaConfigurationServiceAdapter(final OperatorConfigurationPort operatorConfigurationPort) {
        this.operatorConfigurationPort = operatorConfigurationPort;
    }

    @Override
    public DeliveryAreaPolicies currentPolicies() {
        final DepartmentConfigurations configurations = operatorConfigurationPort.getDepartmentConfigurations();
        return new DeliveryAreaPolicies(toRules(configurations.defaults()),
                configurations.departments().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> toRules(entry.getValue()))));
    }

    private DeliveryAreaRules toRules(final DepartmentConfiguration configuration) {
        final DepartmentDeliveryAreaPolicy policy = configuration.getDeliveryAreaPolicy();
        return new DeliveryAreaRules(configuration.isAutoAssignDeliveryAreas(),
                policy.isAllowManualDeliveryAreaEdit(),
                policy.isRecalculateAreasOnStatusChange(),
                policy.getDeliveryAreaMode() == DeliveryAreaMode.RADIUS ? policy.getMaxDeliveryRadiusKm() : 0);
    }
}
