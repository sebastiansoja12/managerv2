package com.warehouse.geocoding.infrastructure.adapter.primary.mapper;

import static com.warehouse.geocoding.GeocodingTestFixtures.CONFIGURATION_ID;
import static com.warehouse.geocoding.GeocodingTestFixtures.apiRequest;
import static com.warehouse.geocoding.GeocodingTestFixtures.configuration;
import static com.warehouse.geocoding.GeocodingTestFixtures.createDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationCreateCommand;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationUpdateCommand;
import com.warehouse.geocoding.infrastructure.adapter.primary.api.GeocodingConfigurationApiResponse;
import com.warehouse.infrastructure.dto.GeocodingConfigurationDto;

class GeocodingConfigurationApiMapperTest {

    @ParameterizedTest
    @MethodSource("createCommandFields")
    void shouldMapApiRequestToCreateCommand(final String fieldName,
                                            final Function<GeocodingConfigurationCreateCommand, Object> fieldValue,
                                            final Object expectedValue) {
        final GeocodingConfigurationCreateCommand command =
                GeocodingConfigurationApiMapper.toCreateCommand(apiRequest());

        assertEquals(expectedValue, fieldValue.apply(command), fieldName);
    }

    @ParameterizedTest
    @MethodSource("createCommandFields")
    void shouldMapCreateDtoToCreateCommand(final String fieldName,
                                           final Function<GeocodingConfigurationCreateCommand, Object> fieldValue,
                                           final Object expectedValue) {
        final GeocodingConfigurationCreateCommand command =
                GeocodingConfigurationApiMapper.toCreateCommand(createDto());

        assertEquals(expectedValue, fieldValue.apply(command), fieldName);
    }

    @ParameterizedTest
    @MethodSource("updateCommandFields")
    void shouldMapApiRequestToUpdateCommand(final String fieldName,
                                            final Function<GeocodingConfigurationUpdateCommand, Object> fieldValue,
                                            final Object expectedValue) {
        final GeocodingConfigurationUpdateCommand command =
                GeocodingConfigurationApiMapper.toUpdateCommand(CONFIGURATION_ID, apiRequest());

        assertEquals(expectedValue, fieldValue.apply(command), fieldName);
    }

    @ParameterizedTest
    @MethodSource("apiResponseFields")
    void shouldMapConfigurationToApiResponse(final String fieldName,
                                             final Function<GeocodingConfigurationApiResponse, Object> fieldValue,
                                             final Object expectedValue) {
        final GeocodingConfigurationApiResponse response =
                GeocodingConfigurationApiMapper.toResponse(configuration());

        assertEquals(expectedValue, fieldValue.apply(response), fieldName);
    }

    @ParameterizedTest
    @MethodSource("dtoFields")
    void shouldMapConfigurationToDto(final String fieldName,
                                     final Function<GeocodingConfigurationDto, Object> fieldValue,
                                     final Object expectedValue) {
        final GeocodingConfigurationDto response =
                GeocodingConfigurationApiMapper.toDto(configuration());

        assertEquals(expectedValue, fieldValue.apply(response), fieldName);
    }

    private static Stream<Arguments> createCommandFields() {
        return Stream.of(
                Arguments.of("apiUserName",
                        (Function<GeocodingConfigurationCreateCommand, Object>) GeocodingConfigurationCreateCommand::apiUserName,
                        "api-user"),
                Arguments.of("apiPassword",
                        (Function<GeocodingConfigurationCreateCommand, Object>) GeocodingConfigurationCreateCommand::apiPassword,
                        "api-password"),
                Arguments.of("apiKey",
                        (Function<GeocodingConfigurationCreateCommand, Object>) GeocodingConfigurationCreateCommand::apiKey,
                        "api-key"),
                Arguments.of("clientNumber",
                        (Function<GeocodingConfigurationCreateCommand, Object>) GeocodingConfigurationCreateCommand::clientNumber,
                        "client-number"),
                Arguments.of("accessToken",
                        (Function<GeocodingConfigurationCreateCommand, Object>) GeocodingConfigurationCreateCommand::accessToken,
                        "access-token"),
                Arguments.of("refreshToken",
                        (Function<GeocodingConfigurationCreateCommand, Object>) GeocodingConfigurationCreateCommand::refreshToken,
                        "refresh-token"),
                Arguments.of("enabled",
                        (Function<GeocodingConfigurationCreateCommand, Object>) GeocodingConfigurationCreateCommand::enabled,
                        true),
                Arguments.of("defaultProvider",
                        (Function<GeocodingConfigurationCreateCommand, Object>) GeocodingConfigurationCreateCommand::defaultProvider,
                        true),
                Arguments.of("provider",
                        (Function<GeocodingConfigurationCreateCommand, Object>) GeocodingConfigurationCreateCommand::provider,
                        GeocodingProvider.GEOAPIFY)
        );
    }

    private static Stream<Arguments> updateCommandFields() {
        return Stream.of(
                Arguments.of("geocodingConfigurationId",
                        (Function<GeocodingConfigurationUpdateCommand, Object>) GeocodingConfigurationUpdateCommand::geocodingConfigurationId,
                        CONFIGURATION_ID),
                Arguments.of("apiUserName",
                        (Function<GeocodingConfigurationUpdateCommand, Object>) GeocodingConfigurationUpdateCommand::apiUserName,
                        "api-user"),
                Arguments.of("apiPassword",
                        (Function<GeocodingConfigurationUpdateCommand, Object>) GeocodingConfigurationUpdateCommand::apiPassword,
                        "api-password"),
                Arguments.of("apiKey",
                        (Function<GeocodingConfigurationUpdateCommand, Object>) GeocodingConfigurationUpdateCommand::apiKey,
                        "api-key"),
                Arguments.of("clientNumber",
                        (Function<GeocodingConfigurationUpdateCommand, Object>) GeocodingConfigurationUpdateCommand::clientNumber,
                        "client-number"),
                Arguments.of("accessToken",
                        (Function<GeocodingConfigurationUpdateCommand, Object>) GeocodingConfigurationUpdateCommand::accessToken,
                        "access-token"),
                Arguments.of("refreshToken",
                        (Function<GeocodingConfigurationUpdateCommand, Object>) GeocodingConfigurationUpdateCommand::refreshToken,
                        "refresh-token"),
                Arguments.of("enabled",
                        (Function<GeocodingConfigurationUpdateCommand, Object>) GeocodingConfigurationUpdateCommand::enabled,
                        true),
                Arguments.of("defaultProvider",
                        (Function<GeocodingConfigurationUpdateCommand, Object>) GeocodingConfigurationUpdateCommand::defaultProvider,
                        true),
                Arguments.of("provider",
                        (Function<GeocodingConfigurationUpdateCommand, Object>) GeocodingConfigurationUpdateCommand::provider,
                        GeocodingProvider.GEOAPIFY)
        );
    }

    private static Stream<Arguments> apiResponseFields() {
        return Stream.of(
                Arguments.of("geocodingConfigurationId",
                        (Function<GeocodingConfigurationApiResponse, Object>) GeocodingConfigurationApiResponse::geocodingConfigurationId,
                        CONFIGURATION_ID),
                Arguments.of("apiUrl",
                        (Function<GeocodingConfigurationApiResponse, Object>) GeocodingConfigurationApiResponse::apiUrl,
                        GeocodingProvider.GEOAPIFY.getUrl()),
                Arguments.of("apiUserName",
                        (Function<GeocodingConfigurationApiResponse, Object>) GeocodingConfigurationApiResponse::apiUserName,
                        "api-user"),
                Arguments.of("apiPassword",
                        (Function<GeocodingConfigurationApiResponse, Object>) GeocodingConfigurationApiResponse::apiPassword,
                        "api-password"),
                Arguments.of("apiKey",
                        (Function<GeocodingConfigurationApiResponse, Object>) GeocodingConfigurationApiResponse::apiKey,
                        "api-key"),
                Arguments.of("clientNumber",
                        (Function<GeocodingConfigurationApiResponse, Object>) GeocodingConfigurationApiResponse::clientNumber,
                        "client-number"),
                Arguments.of("accessToken",
                        (Function<GeocodingConfigurationApiResponse, Object>) GeocodingConfigurationApiResponse::accessToken,
                        "access-token"),
                Arguments.of("refreshToken",
                        (Function<GeocodingConfigurationApiResponse, Object>) GeocodingConfigurationApiResponse::refreshToken,
                        "refresh-token"),
                Arguments.of("enabled",
                        (Function<GeocodingConfigurationApiResponse, Object>) GeocodingConfigurationApiResponse::enabled,
                        true),
                Arguments.of("defaultProvider",
                        (Function<GeocodingConfigurationApiResponse, Object>) GeocodingConfigurationApiResponse::defaultProvider,
                        true),
                Arguments.of("provider",
                        (Function<GeocodingConfigurationApiResponse, Object>) GeocodingConfigurationApiResponse::provider,
                        GeocodingProvider.GEOAPIFY)
        );
    }

    private static Stream<Arguments> dtoFields() {
        return Stream.of(
                Arguments.of("geocodingConfigurationId",
                        (Function<GeocodingConfigurationDto, Object>) GeocodingConfigurationDto::geocodingConfigurationId,
                        CONFIGURATION_ID),
                Arguments.of("apiUrl",
                        (Function<GeocodingConfigurationDto, Object>) GeocodingConfigurationDto::apiUrl,
                        GeocodingProvider.GEOAPIFY.getUrl()),
                Arguments.of("apiUserName",
                        (Function<GeocodingConfigurationDto, Object>) GeocodingConfigurationDto::apiUserName,
                        "api-user"),
                Arguments.of("apiPassword",
                        (Function<GeocodingConfigurationDto, Object>) GeocodingConfigurationDto::apiPassword,
                        "api-password"),
                Arguments.of("apiKey",
                        (Function<GeocodingConfigurationDto, Object>) GeocodingConfigurationDto::apiKey,
                        "api-key"),
                Arguments.of("clientNumber",
                        (Function<GeocodingConfigurationDto, Object>) GeocodingConfigurationDto::clientNumber,
                        "client-number"),
                Arguments.of("accessToken",
                        (Function<GeocodingConfigurationDto, Object>) GeocodingConfigurationDto::accessToken,
                        "access-token"),
                Arguments.of("refreshToken",
                        (Function<GeocodingConfigurationDto, Object>) GeocodingConfigurationDto::refreshToken,
                        "refresh-token"),
                Arguments.of("enabled",
                        (Function<GeocodingConfigurationDto, Object>) GeocodingConfigurationDto::enabled,
                        true),
                Arguments.of("defaultProvider",
                        (Function<GeocodingConfigurationDto, Object>) GeocodingConfigurationDto::defaultProvider,
                        true),
                Arguments.of("provider",
                        (Function<GeocodingConfigurationDto, Object>) GeocodingConfigurationDto::provider,
                        GeocodingProvider.GEOAPIFY)
        );
    }
}
