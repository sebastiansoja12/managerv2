package com.warehouse.geocoding;

import java.util.UUID;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationCreateCommand;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationUpdateCommand;
import com.warehouse.geocoding.infrastructure.adapter.primary.api.GeocodingConfigurationApiRequest;
import com.warehouse.geocoding.infrastructure.adapter.secondary.entity.GeocodingConfigurationEntity;
import com.warehouse.infrastructure.dto.GeocodingConfigurationCreateDto;

public final class GeocodingTestFixtures {

    public static final GeocodingConfigurationId CONFIGURATION_ID =
            new GeocodingConfigurationId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
    public static final GeocodingConfigurationId OTHER_CONFIGURATION_ID =
            new GeocodingConfigurationId(UUID.fromString("22222222-2222-2222-2222-222222222222"));
    public static final OperatorId OPERATOR_ID = OperatorId.of(77L);

    private GeocodingTestFixtures() {
    }

    public static GeocodingConfiguration configuration() {
        final GeocodingConfiguration configuration = new GeocodingConfiguration(
                CONFIGURATION_ID,
                GeocodingProvider.GEOAPIFY.getUrl(),
                "api-user",
                "api-password",
                "api-key",
                "client-number",
                "access-token",
                "refresh-token",
                true,
                true,
                GeocodingProvider.GEOAPIFY);
        configuration.assignOperator(OPERATOR_ID);
        return configuration;
    }

    public static GeocodingConfiguration otherConfiguration() {
        final GeocodingConfiguration configuration = new GeocodingConfiguration(
                OTHER_CONFIGURATION_ID,
                GeocodingProvider.POSITION_STACK.getUrl(),
                "other-api-user",
                "other-api-password",
                "other-api-key",
                "other-client-number",
                "other-access-token",
                "other-refresh-token",
                true,
                true,
                GeocodingProvider.POSITION_STACK);
        configuration.assignOperator(OPERATOR_ID);
        return configuration;
    }

    public static GeocodingConfiguration disabledConfiguration() {
        final GeocodingConfiguration configuration = new GeocodingConfiguration(
                OTHER_CONFIGURATION_ID,
                GeocodingProvider.POSITION_STACK.getUrl(),
                "disabled-api-user",
                "disabled-api-password",
                "disabled-api-key",
                "disabled-client-number",
                "disabled-access-token",
                "disabled-refresh-token",
                false,
                false,
                GeocodingProvider.POSITION_STACK);
        configuration.assignOperator(OPERATOR_ID);
        return configuration;
    }

    public static GeocodingConfigurationUpdateCommand updateCommand() {
        return new GeocodingConfigurationUpdateCommand(
                CONFIGURATION_ID,
                "updated-api-user",
                "updated-api-password",
                "updated-api-key",
                "updated-client-number",
                "updated-access-token",
                "updated-refresh-token",
                false,
                false,
                GeocodingProvider.POSITION_STACK);
    }

    public static GeocodingConfigurationCreateCommand createCommand() {
        return new GeocodingConfigurationCreateCommand(
                "api-user",
                "api-password",
                "api-key",
                "client-number",
                "access-token",
                "refresh-token",
                true,
                true,
                GeocodingProvider.GEOAPIFY);
    }

    public static GeocodingConfigurationApiRequest apiRequest() {
        return new GeocodingConfigurationApiRequest(
                "api-user",
                "api-password",
                "api-key",
                "client-number",
                "access-token",
                "refresh-token",
                true,
                true,
                GeocodingProvider.GEOAPIFY);
    }

    public static GeocodingConfigurationCreateDto createDto() {
        return new GeocodingConfigurationCreateDto(
                "api-user",
                "api-password",
                "api-key",
                "client-number",
                "access-token",
                "refresh-token",
                true,
                true,
                GeocodingProvider.GEOAPIFY);
    }

    public static GeocodingConfigurationEntity entity() {
        final GeocodingConfigurationEntity entity = new GeocodingConfigurationEntity(
                CONFIGURATION_ID,
                GeocodingProvider.GEOAPIFY.getUrl(),
                "api-user",
                "api-password",
                "api-key",
                "client-number",
                "access-token",
                "refresh-token",
                true,
                true,
                GeocodingProvider.GEOAPIFY);
        entity.assignOperator(OPERATOR_ID);
        return entity;
    }
}
