package com.warehouse.mcp.infrastructure.adapter.secondary;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.warehouse.mcp.application.port.secondary.ConfigurationServicePort;
import com.warehouse.mcp.domain.vo.ConfigurationSection;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.CommunicationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.FleetConfigurationDto;
import com.warehouse.organisationstructure.api.dto.PickupPointConfigurationDto;
import com.warehouse.organisationstructure.api.dto.RegionalConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.SortingConfigurationDto;

public class ConfigurationServiceAdapter implements ConfigurationServicePort {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    private final OperatorConfigurationApiService operatorConfigurationApiService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public ConfigurationServiceAdapter(final OperatorConfigurationApiService operatorConfigurationApiService) {
        this.operatorConfigurationApiService = operatorConfigurationApiService;
    }

    @Override
    public Map<String, Object> find(final ConfigurationSection section) {
        final Object configuration = switch (section) {
            case SHIPMENT -> operatorConfigurationApiService.getCurrentShipmentConfiguration();
            case DEPARTMENTS -> operatorConfigurationApiService.getCurrentDepartmentConfiguration();
            case FLEET -> operatorConfigurationApiService.getCurrentFleetConfiguration();
            case COMMUNICATION -> operatorConfigurationApiService.getCurrentCommunicationConfiguration();
            case REGIONAL -> operatorConfigurationApiService.getCurrentRegionalConfiguration();
            case SORTING -> operatorConfigurationApiService.getCurrentSortingConfiguration();
            case PICKUP_POINTS -> operatorConfigurationApiService.getCurrentPickupPointConfiguration();
        };
        return toMap(configuration);
    }

    @Override
    public Map<String, Object> update(final ConfigurationSection section, final Map<String, Object> configuration) {
        final Object updated = switch (section) {
            case SHIPMENT -> operatorConfigurationApiService.updateCurrentShipmentConfiguration(
                    convert(configuration, ShipmentConfigurationDto.class));
            case DEPARTMENTS -> operatorConfigurationApiService.updateCurrentDepartmentConfiguration(
                    convert(configuration, DepartmentConfigurationDto.class));
            case FLEET -> operatorConfigurationApiService.updateCurrentFleetConfiguration(
                    convert(configuration, FleetConfigurationDto.class));
            case COMMUNICATION -> operatorConfigurationApiService.updateCurrentCommunicationConfiguration(
                    convert(configuration, CommunicationConfigurationDto.class));
            case REGIONAL -> operatorConfigurationApiService.updateCurrentRegionalConfiguration(
                    convert(configuration, RegionalConfigurationDto.class));
            case SORTING -> operatorConfigurationApiService.updateCurrentSortingConfiguration(
                    convert(configuration, SortingConfigurationDto.class));
            case PICKUP_POINTS -> operatorConfigurationApiService.updateCurrentPickupPointConfiguration(
                    convert(configuration, PickupPointConfigurationDto.class));
        };
        return toMap(updated);
    }

    private Map<String, Object> toMap(final Object configuration) {
        return configuration == null ? Map.of() : this.objectMapper.convertValue(configuration, MAP_TYPE);
    }

    private <T> T convert(final Map<String, Object> configuration, final Class<T> type) {
        try {
            return this.objectMapper.convertValue(configuration, type);
        } catch (final IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid configuration value: "
                    + exception.getMessage().lines().findFirst().orElse(""), exception);
        }
    }
}
