package com.warehouse.deliverynetwork.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.application.port.secondary.DeliveryNetworkConfigurationServicePort;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnectionPolicies;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnectionRules;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationsDto;
import com.warehouse.organisationstructure.api.dto.DepartmentConnectionPolicyDto;

import java.util.stream.Collectors;

public class DeliveryNetworkConfigurationServiceAdapter implements DeliveryNetworkConfigurationServicePort {

    private final OperatorConfigurationApiService operatorConfigurationApiService;

    public DeliveryNetworkConfigurationServiceAdapter(
            final OperatorConfigurationApiService operatorConfigurationApiService) {
        this.operatorConfigurationApiService = operatorConfigurationApiService;
    }

    @Override
    public DepartmentConnectionPolicies currentPolicies() {
        final DepartmentConfigurationsDto configurations =
                this.operatorConfigurationApiService.getDepartmentConfigurations();
        if (configurations == null) {
            return DepartmentConnectionPolicies.uniform(DepartmentConnectionRules.defaultRules());
        }
        return new DepartmentConnectionPolicies(toRules(configurations.defaults()),
                configurations.departments().entrySet().stream()
                        .collect(Collectors.toMap(entry -> new DepartmentId(entry.getKey()),
                                entry -> toRules(entry.getValue()))));
    }

    private DepartmentConnectionRules toRules(final DepartmentConfigurationDto configuration) {
        if (configuration == null || configuration.connectionPolicy() == null) {
            return DepartmentConnectionRules.defaultRules();
        }
        final DepartmentConnectionPolicyDto connectionPolicy = configuration.connectionPolicy();
        return new DepartmentConnectionRules(
                configuration.autoConnectDepartments(),
                connectionPolicy.sortingCenterConnectionCount(),
                connectionPolicy.maxAutoConnectionDistanceKm(),
                connectionPolicy.requireSortingConnectionForActivation(),
                connectionPolicy.bidirectionalConnectionsByDefault());
    }
}
