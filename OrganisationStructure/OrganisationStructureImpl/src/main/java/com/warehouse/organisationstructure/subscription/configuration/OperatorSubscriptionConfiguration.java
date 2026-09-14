package com.warehouse.organisationstructure.subscription.configuration;

import tools.jackson.databind.ObjectMapper;
import com.warehouse.auth.PlatformActorService;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.organisationstructure.subscription.application.port.primary.OperatorSubscriptionPort;
import com.warehouse.organisationstructure.subscription.application.port.primary.OperatorSubscriptionPortImpl;
import com.warehouse.organisationstructure.subscription.application.port.secondary.CurrentOperatorServicePort;
import com.warehouse.organisationstructure.subscription.application.port.secondary.OperatorServicePort;
import com.warehouse.organisationstructure.subscription.application.port.secondary.OperatorSubscriptionRepository;
import com.warehouse.organisationstructure.subscription.application.port.secondary.PlatformActorServicePort;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionPlan;
import com.warehouse.organisationstructure.subscription.domain.vo.StarterSubscription;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.OperatorIdPathConverter;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.OperatorSubscriptionServiceAdapter;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.SubscriptionAccessInterceptor;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary.CurrentOperatorServiceAdapter;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary.OperatorServiceAdapter;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary.OperatorSubscriptionReadRepository;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary.OperatorSubscriptionRepositoryImpl;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary.PlatformActorServiceAdapter;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.api.OperatorSubscriptionApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class OperatorSubscriptionConfiguration {

    @Bean("operatorSubscription.operatorSubscriptionRepository")
    public OperatorSubscriptionRepository operatorSubscriptionRepository(
            final OperatorSubscriptionReadRepository operatorSubscriptionReadRepository) {
        return new OperatorSubscriptionRepositoryImpl(operatorSubscriptionReadRepository, Clock.systemUTC());
    }

    @Bean("operatorSubscription.operatorServicePort")
    public OperatorServicePort operatorServicePort(final OperatorApiService operatorApiService) {
        return new OperatorServiceAdapter(operatorApiService);
    }

    @Bean("operatorSubscription.platformActorServicePort")
    public PlatformActorServicePort platformActorServicePort(final PlatformActorService platformActorService) {
        return new PlatformActorServiceAdapter(platformActorService);
    }

    @Bean("operatorSubscription.currentOperatorServicePort")
    public CurrentOperatorServicePort currentOperatorServicePort(
            final OperatorContextProvider operatorContextProvider) {
        return new CurrentOperatorServiceAdapter(operatorContextProvider);
    }

    @Bean
    public OperatorSubscriptionPort operatorSubscriptionPort(
            final OperatorSubscriptionRepository operatorSubscriptionRepository,
            final OperatorServicePort operatorServicePort,
            final PlatformActorServicePort platformActorServicePort,
            final CurrentOperatorServicePort currentOperatorServicePort,
            @Value("${manager.subscription.starter.plan:MINI}") final SubscriptionPlan starterPlan,
            @Value("${manager.subscription.starter.duration-days:30}") final int starterDurationDays,
            @Value("${manager.subscription.time-zone:Europe/Warsaw}") final String subscriptionTimeZone) {
        return new OperatorSubscriptionPortImpl(operatorSubscriptionRepository, operatorServicePort,
                platformActorServicePort, currentOperatorServicePort,
                new StarterSubscription(starterPlan, starterDurationDays),
                Clock.system(ZoneId.of(subscriptionTimeZone)));
    }

    @Bean
    public OperatorSubscriptionApiService operatorSubscriptionApiService(
            final OperatorSubscriptionPort operatorSubscriptionPort) {
        return new OperatorSubscriptionServiceAdapter(operatorSubscriptionPort);
    }

    @Bean
    public WebMvcConfigurer subscriptionAccessWebMvcConfigurer(final OperatorSubscriptionPort operatorSubscriptionPort,
                                                               final ObjectMapper objectMapper) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(final InterceptorRegistry registry) {
                registry.addInterceptor(new SubscriptionAccessInterceptor(operatorSubscriptionPort, objectMapper))
                        .order(Ordered.HIGHEST_PRECEDENCE + 1);
            }

            @Override
            public void addFormatters(final FormatterRegistry registry) {
                registry.addConverter(new OperatorIdPathConverter());
            }
        };
    }
}
