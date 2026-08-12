package com.warehouse.tracking.configuration;

import java.net.http.HttpClient;
import java.time.Clock;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.commonassets.security.CredentialCipher;
import com.warehouse.tracking.domain.port.secondary.TrackingIntegrationRepository;
import com.warehouse.tracking.domain.port.secondary.TrackingProviderServicePort;
import com.warehouse.tracking.domain.port.secondary.TrackingTokenServicePort;
import com.warehouse.tracking.domain.service.TrackingIntegrationService;
import com.warehouse.tracking.domain.service.TrackingProviderRegistry;
import com.warehouse.tracking.infrastructure.adapter.secondary.TrackingIntegrationRepositoryAdapter;
import com.warehouse.tracking.infrastructure.adapter.secondary.entity.TrackingIntegrationConfigurationEntity;
import com.warehouse.tracking.infrastructure.adapter.secondary.inpost.InPostOAuthClient;
import com.warehouse.tracking.infrastructure.adapter.secondary.inpost.InPostTokenManager;
import com.warehouse.tracking.infrastructure.adapter.secondary.inpost.InPostTrackingClient;
import com.warehouse.tracking.infrastructure.adapter.secondary.inpost.InPostTrackingServiceAdapter;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.RetryConfig;

@Configuration
public class TrackingConfiguration {

    @Bean
    public RestClient inPostRestClient(
            @Value("${tracking.inpost.connect-timeout:3s}") final Duration connectTimeout,
            @Value("${tracking.inpost.read-timeout:8s}") final Duration readTimeout) {
        final HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
        final JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout);
        return RestClient.builder().requestFactory(requestFactory).build();
    }

    @Bean("inPostRetryConfig")
    public RetryConfig inPostRetryConfig(
            @Value("${tracking.inpost.retry.max-attempts:3}") final int maxAttempts,
            @Value("${tracking.inpost.retry.initial-delay:200ms}") final Duration initialDelay,
            @Value("${tracking.inpost.retry.max-delay:2s}") final Duration maxDelay) {
        return RetryConfig.custom()
                .maxAttempts(maxAttempts)
                .intervalFunction(IntervalFunction.ofExponentialRandomBackoff(
                        initialDelay, 2.0, 0.5, maxDelay))
                .retryOnException(exception -> exception instanceof com.warehouse.tracking.infrastructure.adapter.secondary.inpost.InPostRequestException
                        && ((com.warehouse.tracking.infrastructure.adapter.secondary.inpost.InPostRequestException) exception).isRetryable())
                .build();
    }

    @Bean
    public Clock trackingClock() {
        return Clock.systemUTC();
    }

    @Bean
    public InPostOAuthClient inPostOAuthClient(final RestClient inPostRestClient,
                                               final ObjectMapper objectMapper) {
        return new InPostOAuthClient(inPostRestClient, objectMapper);
    }

    @Bean
    public InPostTrackingClient inPostTrackingClient(
            final RestClient inPostRestClient,
            final ObjectMapper objectMapper,
            @Qualifier("inPostRetryConfig") final RetryConfig retryConfig) {
        return new InPostTrackingClient(inPostRestClient, objectMapper, retryConfig);
    }

    @Bean
    public TrackingTokenServicePort trackingTokenServicePort(
            final InPostOAuthClient oauthClient,
            @Qualifier("inPostRetryConfig") final RetryConfig retryConfig,
            final Clock trackingClock) {
        return new InPostTokenManager(oauthClient, retryConfig, trackingClock);
    }

    @Bean
    public TrackingIntegrationRepository trackingIntegrationRepository(
            final OperatorFilteredRepository<TrackingIntegrationConfigurationEntity> repository,
            final CredentialCipher credentialCipher,
            final ObjectMapper objectMapper) {
        return new TrackingIntegrationRepositoryAdapter(repository, credentialCipher, objectMapper);
    }

    @Bean
    public TrackingProviderServicePort inPostTrackingProviderServicePort(
            final TrackingIntegrationRepository trackingIntegrationRepository,
            final TrackingTokenServicePort trackingTokenServicePort,
            final InPostTrackingClient inPostTrackingClient) {
        return new InPostTrackingServiceAdapter(
                trackingIntegrationRepository, trackingTokenServicePort, inPostTrackingClient);
    }

    @Bean
    public TrackingProviderRegistry trackingProviderRegistry(final List<TrackingProviderServicePort> providers) {
        return new TrackingProviderRegistry(providers);
    }

    @Bean
    public TrackingIntegrationService trackingIntegrationService(
            final TrackingIntegrationRepository trackingIntegrationRepository,
            final TrackingTokenServicePort trackingTokenServicePort,
            final TrackingProviderRegistry trackingProviderRegistry) {
        return new TrackingIntegrationService(
                trackingIntegrationRepository, trackingTokenServicePort, trackingProviderRegistry);
    }
}
