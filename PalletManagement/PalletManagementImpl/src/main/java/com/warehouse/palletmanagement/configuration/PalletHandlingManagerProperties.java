package com.warehouse.palletmanagement.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties(prefix = "pallet-handling-manager")
public record PalletHandlingManagerProperties(
        @DefaultValue("http://localhost:8071") String url,
        @DefaultValue("1s") Duration connectTimeout,
        @DefaultValue("3s") Duration readTimeout,
        @DefaultValue("20") int slidingWindowSize,
        @DefaultValue("10") int minimumNumberOfCalls,
        @DefaultValue("50") float failureRateThreshold,
        @DefaultValue("2s") Duration slowCallDuration,
        @DefaultValue("80") float slowCallRateThreshold,
        @DefaultValue("30s") Duration waitDurationInOpenState,
        @DefaultValue("3") int permittedCallsInHalfOpenState,
        @DefaultValue("2") int readMaxAttempts,
        @DefaultValue("200ms") Duration readRetryWait) {
}
