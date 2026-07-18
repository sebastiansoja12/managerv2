package com.warehouse.auth.domain.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.provider.ApiKeyProvider;
import com.warehouse.auth.domain.vo.ApiKey;
import com.warehouse.commonassets.identificator.UserId;

public class ApiEncoderImpl implements ApiKeyEncoder {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final ApiKeyProvider apiKeyProvider;

    public ApiEncoderImpl(final ApiKeyProvider apiKeyProvider) {
        this.apiKeyProvider = apiKeyProvider;
    }

    @Override
    public ApiKey encode(final User user) {
        return encode(user.getUserId(), user.getApiKey());
    }

    @Override
    public ApiKey encode(final UserId userId, final String apiKey) {
        return new ApiKey(userId, hash(apiKey));
    }

    @Override
    public ApiKey decode(final ApiKey apiKey) {
        return new ApiKey(apiKey.userId(), hash(apiKey.key()));
    }

    private String hash(final String apiKey) {
        try {
            final Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(apiKeyProvider.getKey().getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(mac.doFinal(apiKey.getBytes(StandardCharsets.UTF_8)));
        } catch (final Exception exception) {
            throw new IllegalStateException("API key hashing is not available", exception);
        }
    }
}
