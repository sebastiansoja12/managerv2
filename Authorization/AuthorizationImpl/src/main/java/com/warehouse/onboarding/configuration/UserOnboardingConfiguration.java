package com.warehouse.onboarding.configuration;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.onboarding.application.port.primary.UserOnboardingPort;
import com.warehouse.onboarding.application.port.primary.UserOnboardingPortImpl;
import com.warehouse.onboarding.application.port.secondary.UserOnboardingRepository;
import com.warehouse.onboarding.domain.vo.ConsentVersion;
import com.warehouse.onboarding.infrastructure.adapter.secondary.CurrentUserServiceAdapter;
import com.warehouse.onboarding.infrastructure.adapter.secondary.UserOnboardingRepositoryImpl;
import com.warehouse.onboarding.infrastructure.adapter.secondary.entity.UserOnboardingEntity;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class UserOnboardingConfiguration {

    @Bean
    public UserOnboardingRepository userOnboardingRepository(final EntityManager entityManager,
                                                             final OperatorContextProvider operatorContextProvider) {
        return new UserOnboardingRepositoryImpl(
                new BaseRepository<UserOnboardingEntity>(entityManager, operatorContextProvider));
    }

    @Bean
    public UserOnboardingPort userOnboardingPort(
            final UserOnboardingRepository userOnboardingRepository,
            final CurrentUserApiService currentUserApiService,
            @Value("${manager.auth.consents.version:2026-11}") final String consentVersion) {
        return new UserOnboardingPortImpl(userOnboardingRepository,
                new CurrentUserServiceAdapter(currentUserApiService), Clock.systemUTC(),
                new ConsentVersion(consentVersion));
    }
}
