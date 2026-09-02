package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.platformadministration.application.port.secondary.OperatorShipmentSettingsServicePort;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.domain.vo.OperatorShipmentSettings;

import java.util.Map;
import java.util.Optional;

public class OperatorShipmentSettingsServiceAdapter implements OperatorShipmentSettingsServicePort {

    private static final TypeReference<Map<String, Object>> SETTINGS_TYPE = new TypeReference<>() {
    };

    private final OperatorConfigurationApiService operatorConfigurationApiService;

    private final OperatorScopeExecutor operatorScopeExecutor;

    private final ObjectMapper objectMapper;

    public OperatorShipmentSettingsServiceAdapter(final OperatorConfigurationApiService operatorConfigurationApiService,
                                                  final OperatorScopeExecutor operatorScopeExecutor,
                                                  final ObjectMapper objectMapper) {
        this.operatorConfigurationApiService = operatorConfigurationApiService;
        this.operatorScopeExecutor = operatorScopeExecutor;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<OperatorShipmentSettings> find(final OperatorScope operatorScope) {
        final ShipmentConfigurationDto configuration = operatorScopeExecutor.execute(operatorScope,
                operatorConfigurationApiService::getCurrentShipmentConfiguration);
        return Optional.ofNullable(configuration).map(this::toSettings);
    }

    @Override
    public OperatorShipmentSettings update(final OperatorScope operatorScope, final OperatorShipmentSettings settings) {
        final ShipmentConfigurationDto configuration = objectMapper.convertValue(settings.values(),
                ShipmentConfigurationDto.class);
        return toSettings(operatorScopeExecutor.execute(operatorScope,
                () -> operatorConfigurationApiService.updateCurrentShipmentConfiguration(configuration)));
    }

    private OperatorShipmentSettings toSettings(final ShipmentConfigurationDto configuration) {
        return new OperatorShipmentSettings(objectMapper.convertValue(configuration, SETTINGS_TYPE));
    }
}
