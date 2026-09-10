package com.warehouse.geocoding.infrastructure.adapter.secondary;

import static com.warehouse.geocoding.GeocodingTestFixtures.CONFIGURATION_ID;
import static com.warehouse.geocoding.GeocodingTestFixtures.OPERATOR_ID;
import static com.warehouse.geocoding.GeocodingTestFixtures.configuration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.security.CredentialCipher;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.infrastructure.adapter.secondary.entity.GeocodingConfigurationEntity;

class GeocodingConfigurationMapperTest {

    private static final CredentialCipher CREDENTIAL_CIPHER = new CredentialCipher("geocoding-test-secret");

    @ParameterizedTest
    @MethodSource("plainEntityFields")
    void shouldMapPlainFieldToEntity(final String fieldName,
                                     final Function<GeocodingConfigurationEntity, Object> fieldValue,
                                     final Object expectedValue) {
        final GeocodingConfigurationEntity entity =
                GeocodingConfigurationMapper.toEntity(configuration(), CREDENTIAL_CIPHER);

        assertEquals(expectedValue, fieldValue.apply(entity), fieldName);
    }

    @ParameterizedTest
    @MethodSource("encryptedEntityFields")
    void shouldEncryptCredentialFieldToEntity(final String fieldName,
                                              final Function<GeocodingConfigurationEntity, String> fieldValue,
                                              final String rawCredential) {
        final GeocodingConfigurationEntity entity =
                GeocodingConfigurationMapper.toEntity(configuration(), CREDENTIAL_CIPHER);

        assertNotEquals(rawCredential, fieldValue.apply(entity), fieldName);
        assertEquals(rawCredential, CREDENTIAL_CIPHER.decrypt(fieldValue.apply(entity)), fieldName);
    }

    @ParameterizedTest
    @MethodSource("modelFields")
    void shouldMapEntityToModel(final String fieldName,
                                final Function<GeocodingConfiguration, Object> fieldValue,
                                final Object expectedValue) {
        final GeocodingConfigurationEntity encryptedEntity =
                GeocodingConfigurationMapper.toEntity(configuration(), CREDENTIAL_CIPHER);

        final GeocodingConfiguration model =
                GeocodingConfigurationMapper.toModel(encryptedEntity, CREDENTIAL_CIPHER);

        assertEquals(expectedValue, fieldValue.apply(model), fieldName);
    }

    @ParameterizedTest
    @MethodSource("nullableCredentialFields")
    void shouldKeepNullCredentialsWhenMappingToEntity(final String fieldName,
                                                      final Function<GeocodingConfigurationEntity, String> fieldValue) {
        final GeocodingConfiguration configuration = new GeocodingConfiguration(
                CONFIGURATION_ID,
                GeocodingProvider.GEOAPIFY.getUrl(),
                "api-user",
                null,
                null,
                "client-number",
                null,
                null,
                true,
                true,
                GeocodingProvider.GEOAPIFY);

        final GeocodingConfigurationEntity entity =
                GeocodingConfigurationMapper.toEntity(configuration, CREDENTIAL_CIPHER);

        assertEquals(null, fieldValue.apply(entity), fieldName);
    }

    @ParameterizedTest
    @MethodSource("rawCredentialEntityFields")
    void shouldKeepRawCredentialWhenDecryptingLegacyValue(final String fieldName,
                                                          final Function<GeocodingConfiguration, String> fieldValue,
                                                          final String expectedValue) {
        final GeocodingConfigurationEntity entity = new GeocodingConfigurationEntity(
                CONFIGURATION_ID,
                GeocodingProvider.GEOAPIFY.getUrl(),
                "api-user",
                "legacy-password",
                "legacy-key",
                "client-number",
                "legacy-access",
                "legacy-refresh",
                true,
                true,
                GeocodingProvider.GEOAPIFY);

        final GeocodingConfiguration model = GeocodingConfigurationMapper.toModel(entity, CREDENTIAL_CIPHER);

        assertEquals(expectedValue, fieldValue.apply(model), fieldName);
    }

    private static Stream<Arguments> plainEntityFields() {
        return Stream.of(
                Arguments.of("geocodingConfigurationId",
                        (Function<GeocodingConfigurationEntity, Object>) GeocodingConfigurationEntity::getGeocodingConfigurationId,
                        CONFIGURATION_ID),
                Arguments.of("apiUrl",
                        (Function<GeocodingConfigurationEntity, Object>) GeocodingConfigurationEntity::getApiUrl,
                        GeocodingProvider.GEOAPIFY.getUrl()),
                Arguments.of("apiUserName",
                        (Function<GeocodingConfigurationEntity, Object>) GeocodingConfigurationEntity::getApiUserName,
                        "api-user"),
                Arguments.of("clientNumber",
                        (Function<GeocodingConfigurationEntity, Object>) GeocodingConfigurationEntity::getClientNumber,
                        "client-number"),
                Arguments.of("enabled",
                        (Function<GeocodingConfigurationEntity, Object>) GeocodingConfigurationEntity::isEnabled,
                        true),
                Arguments.of("defaultProvider",
                        (Function<GeocodingConfigurationEntity, Object>) GeocodingConfigurationEntity::isDefaultProvider,
                        true),
                Arguments.of("provider",
                        (Function<GeocodingConfigurationEntity, Object>) GeocodingConfigurationEntity::getProvider,
                        GeocodingProvider.GEOAPIFY),
                Arguments.of("operatorId",
                        (Function<GeocodingConfigurationEntity, Object>) GeocodingConfigurationEntity::operatorId,
                        OPERATOR_ID)
        );
    }

    private static Stream<Arguments> encryptedEntityFields() {
        return Stream.of(
                Arguments.of("apiPassword",
                        (Function<GeocodingConfigurationEntity, String>) GeocodingConfigurationEntity::getApiPassword,
                        "api-password"),
                Arguments.of("apiKey",
                        (Function<GeocodingConfigurationEntity, String>) GeocodingConfigurationEntity::getApiKey,
                        "api-key"),
                Arguments.of("accessToken",
                        (Function<GeocodingConfigurationEntity, String>) GeocodingConfigurationEntity::getAccessToken,
                        "access-token"),
                Arguments.of("refreshToken",
                        (Function<GeocodingConfigurationEntity, String>) GeocodingConfigurationEntity::getRefreshToken,
                        "refresh-token")
        );
    }

    private static Stream<Arguments> modelFields() {
        return Stream.of(
                Arguments.of("geocodingConfigurationId",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getGeocodingConfigurationId,
                        CONFIGURATION_ID),
                Arguments.of("apiUrl",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getApiUrl,
                        GeocodingProvider.GEOAPIFY.getUrl()),
                Arguments.of("apiUserName",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getApiUserName,
                        "api-user"),
                Arguments.of("apiPassword",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getApiPassword,
                        "api-password"),
                Arguments.of("apiKey",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getApiKey,
                        "api-key"),
                Arguments.of("clientNumber",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getClientNumber,
                        "client-number"),
                Arguments.of("accessToken",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getAccessToken,
                        "access-token"),
                Arguments.of("refreshToken",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getRefreshToken,
                        "refresh-token"),
                Arguments.of("enabled",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::isEnabled,
                        true),
                Arguments.of("defaultProvider",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::isDefaultProvider,
                        true),
                Arguments.of("provider",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::getProvider,
                        GeocodingProvider.GEOAPIFY),
                Arguments.of("operatorId",
                        (Function<GeocodingConfiguration, Object>) GeocodingConfiguration::operatorId,
                        OPERATOR_ID)
        );
    }

    private static Stream<Arguments> nullableCredentialFields() {
        return Stream.of(
                Arguments.of("apiPassword",
                        (Function<GeocodingConfigurationEntity, String>) GeocodingConfigurationEntity::getApiPassword),
                Arguments.of("apiKey",
                        (Function<GeocodingConfigurationEntity, String>) GeocodingConfigurationEntity::getApiKey),
                Arguments.of("accessToken",
                        (Function<GeocodingConfigurationEntity, String>) GeocodingConfigurationEntity::getAccessToken),
                Arguments.of("refreshToken",
                        (Function<GeocodingConfigurationEntity, String>) GeocodingConfigurationEntity::getRefreshToken)
        );
    }

    private static Stream<Arguments> rawCredentialEntityFields() {
        return Stream.of(
                Arguments.of("apiPassword",
                        (Function<GeocodingConfiguration, String>) GeocodingConfiguration::getApiPassword,
                        "legacy-password"),
                Arguments.of("apiKey",
                        (Function<GeocodingConfiguration, String>) GeocodingConfiguration::getApiKey,
                        "legacy-key"),
                Arguments.of("accessToken",
                        (Function<GeocodingConfiguration, String>) GeocodingConfiguration::getAccessToken,
                        "legacy-access"),
                Arguments.of("refreshToken",
                        (Function<GeocodingConfiguration, String>) GeocodingConfiguration::getRefreshToken,
                        "legacy-refresh")
        );
    }
}
