package com.warehouse.auth.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.provider.ApiKeyProvider;
import com.warehouse.auth.domain.vo.ApiKey;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

class ApiEncoderImplTest {

    @Test
    void shouldHashApiKeyForUserBeforeSaving() {
        final ApiEncoderImpl encoder = new ApiEncoderImpl(apiKeyProvider());
        final User user = user("plain-api-key");

        final ApiKey apiKey = encoder.encode(user);

        assertThat(apiKey.userId()).isEqualTo(new UserId(10L));
        assertThat(apiKey.key()).isNotEmpty();
        assertThat(apiKey.key()).isNotEqualTo("plain-api-key");
        assertThat(apiKey.key()).doesNotContain("plain-api-key");
        assertThat(apiKey.key()).doesNotContain("api-secret");
    }

    @Test
    void shouldHashApiKeyPassedDirectlyToConstructor() {
        final ApiEncoderImpl encoder = new ApiEncoderImpl(apiKeyProvider());

        final ApiKey apiKey = encoder.encode(new UserId(10L), "plain-api-key");

        assertThat(apiKey.userId()).isEqualTo(new UserId(10L));
        assertThat(apiKey.key()).isNotEqualTo("plain-api-key");
        assertThat(apiKey.key()).doesNotContain("plain-api-key");
        assertThat(apiKey.key()).doesNotContain("api-secret");
    }

    @Test
    void shouldHashIncomingApiKeyUsingSameAlgorithmForLookup() {
        final ApiEncoderImpl encoder = new ApiEncoderImpl(apiKeyProvider());
        final ApiKey savedApiKey = encoder.encode(new UserId(10L), "plain-api-key");

        final ApiKey lookupApiKey = encoder.decode(new ApiKey(new UserId(10L), "plain-api-key"));

        assertThat(lookupApiKey).isEqualTo(savedApiKey);
    }

    @Test
    void shouldCreateDifferentHashesForDifferentApiKeys() {
        final ApiEncoderImpl encoder = new ApiEncoderImpl(apiKeyProvider());

        final ApiKey firstApiKey = encoder.decode(new ApiKey(new UserId(10L), "plain-api-key"));
        final ApiKey secondApiKey = encoder.decode(new ApiKey(new UserId(10L), "other-api-key"));

        assertThat(firstApiKey.key()).isNotEqualTo(secondApiKey.key());
    }

    private ApiKeyProvider apiKeyProvider() {
        final ApiKeyProvider provider = new ApiKeyProvider();
        provider.setKey("api-secret");
        return provider;
    }

    private User user(final String apiKey) {
        return new User(new UserId(10L), "s-soja", "password", "Sebastian", "Soja", "s-soja@test.pl",
                User.Role.USER, new DepartmentCode("TST"), apiKey);
    }
}
