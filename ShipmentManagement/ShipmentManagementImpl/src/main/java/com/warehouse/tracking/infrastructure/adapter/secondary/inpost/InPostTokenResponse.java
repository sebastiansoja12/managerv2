package com.warehouse.tracking.infrastructure.adapter.secondary.inpost;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
record InPostTokenResponse(@JsonProperty("access_token") String accessToken,
                           @JsonProperty("token_type") String tokenType,
                           @JsonProperty("expires_in") long expiresIn) {
}
