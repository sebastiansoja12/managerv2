package com.warehouse.geocoding.domain.model;

import static com.warehouse.geocoding.GeocodingTestFixtures.CONFIGURATION_ID;
import static com.warehouse.geocoding.GeocodingTestFixtures.OPERATOR_ID;
import static com.warehouse.geocoding.GeocodingTestFixtures.configuration;
import static com.warehouse.geocoding.GeocodingTestFixtures.updateCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationSnapshot;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationUpdateCommand;

class GeocodingConfigurationTest {

    @ParameterizedTest
    @MethodSource("snapshotFields")
    void shouldExposeSnapshotField(final String fieldName,
                                   final Function<GeocodingConfigurationSnapshot, Object> fieldValue,
                                   final Object expectedValue) {
        final GeocodingConfigurationSnapshot snapshot = configuration().toSnapshot();

        assertEquals(expectedValue, fieldValue.apply(snapshot), fieldName);
    }

    @ParameterizedTest
    @MethodSource("updatedFields")
    void shouldUpdateField(final String fieldName,
                           final Function<GeocodingConfiguration, Object> fieldValue,
                           final Object expectedValue) {
        final GeocodingConfiguration configuration = configuration();

        configuration.update(updateCommand());

        assertEquals(expectedValue, fieldValue.apply(configuration), fieldName);
    }

    @Test
    void shouldRejectNullProviderWhenCreatingConfiguration() {
        final NullPointerException exception = assertThrows(NullPointerException.class, () -> new GeocodingConfiguration(
                CONFIGURATION_ID,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                true,
                false,
                null));

        assertEquals("Geocoding provider is required", exception.getMessage());
    }

    @Test
    void shouldRejectNullProviderWhenUpdatingConfiguration() {
        final GeocodingConfiguration configuration = configuration();

        final NullPointerException exception = assertThrows(NullPointerException.class, () -> configuration.update(
                new GeocodingConfigurationUpdateCommand(
                        CONFIGURATION_ID,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        true,
                        false,
                        null)));

        assertEquals("Geocoding provider is required", exception.getMessage());
    }

    @Test
    void shouldUnsetDefaultProvider() {
        final GeocodingConfiguration configuration = configuration();

        configuration.unsetDefaultProvider();

        assertFalse(configuration.isDefaultProvider());
    }

    @Test
    void shouldKeepOperatorContextAssignedToConfiguration() {
        final GeocodingConfiguration configuration = configuration();

        assertEquals(OPERATOR_ID, configuration.operatorId());
    }

    @Test
    void shouldKeepEnabledFlagWhenCreatedAsEnabled() {
        assertTrue(configuration().isEnabled());
    }

    private static Stream<Arguments> snapshotFields() {
        return Stream.of(
                Arguments.of("geocodingConfigurationId",
                        (Function<GeocodingConfigurationSnapshot, Object>) GeocodingConfigurationSnapshot::geocodingConfigurationId,
                        CONFIGURATION_ID),
                Arguments.of("apiUrl",
                        (Function<GeocodingConfigurationSnapshot, Object>) GeocodingConfigurationSnapshot::apiUrl,
                        GeocodingProvider.GEOAPIFY.getUrl()),
                Arguments.of("apiUserName",
                        (Function<GeocodingConfigurationSnapshot, Object>) GeocodingConfigurationSnapshot::apiUserName,
                        "api-user"),
                Arguments.of("apiPassword",
                        (Function<GeocodingConfigurationSnapshot, Object>) GeocodingConfigurationSnapshot::apiPassword,
                        "api-password"),
                Arguments.of("apiKey",
                        (Function<GeocodingConfigurationSnapshot, Object>) GeocodingConfigurationSnapshot::apiKey,
                        "api-key"),
                Arguments.of("clientNumber",
                        (Function<GeocodingConfigurationSnapshot, Object>) GeocodingConfigurationSnapshot::clientNumber,
                        "client-number"),
                Arguments.of("accessToken",
                        (Function<GeocodingConfigurationSnapshot, Object>) GeocodingConfigurationSnapshot::accessToken,
                        "access-token"),
                Arguments.of("refreshToken",
                        (Function<GeocodingConfigurationSnapshot, Object>) GeocodingConfigurationSnapshot::refreshToken,
                        "refresh-token"),
                Arguments.of("enabled",
                        (Function<GeocodingConfigurationSnapshot, Object>) GeocodingConfigurationSnapshot::enabled,
                        true),
                Arguments.of("defaultProvider",
                        (Function<GeocodingConfigurationSnapshot, Object>) GeocodingConfigurationSnapshot::defaultProvider,
                        true),
                Arguments.of("provider",
                        (Function<GeocodingConfigurationSnapshot, Object>) GeocodingConfigurationSnapshot::provider,
                        GeocodingProvider.GEOAPIFY)
        );
    }

    private static Stream<Arguments> updatedFields() {
        return Stream.of(
                Arguments.of("apiUrl",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getApiUrl,
                        GeocodingProvider.POSITION_STACK.getUrl()),
                Arguments.of("apiUserName",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getApiUserName,
                        "updated-api-user"),
                Arguments.of("apiPassword",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getApiPassword,
                        "updated-api-password"),
                Arguments.of("apiKey",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getApiKey,
                        "updated-api-key"),
                Arguments.of("clientNumber",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getClientNumber,
                        "updated-client-number"),
                Arguments.of("accessToken",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getAccessToken,
                        "updated-access-token"),
                Arguments.of("refreshToken",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getRefreshToken,
                        "updated-refresh-token"),
                Arguments.of("enabled",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::isEnabled,
                        false),
                Arguments.of("defaultProvider",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::isDefaultProvider,
                        false),
                Arguments.of("provider",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getProvider,
                        GeocodingProvider.POSITION_STACK)
        );
    }
}
