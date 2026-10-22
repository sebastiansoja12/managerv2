package com.warehouse.account.configuration;

import com.warehouse.account.application.port.primary.SupplierAccountPort;
import com.warehouse.account.application.port.primary.SupplierAccountPortImpl;
import com.warehouse.account.application.port.secondary.UserAccountServicePort;
import com.warehouse.account.application.port.secondary.UserInvitationRepository;
import com.warehouse.account.infrastructure.adapter.secondary.UserAccountServiceAdapter;
import com.warehouse.account.infrastructure.adapter.secondary.UserInvitationRepositoryImpl;
import com.warehouse.account.infrastructure.adapter.secondary.entity.UserInvitationEntity;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.NoOperatorContextBaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.Duration;

@Configuration
public class SupplierAccountConfiguration {

    @Bean
    public UserInvitationRepository userInvitationRepository(final EntityManager entityManager,
                                                             final OperatorContextProvider operatorContextProvider) {
        return new UserInvitationRepositoryImpl(
                new BaseRepository<UserInvitationEntity>(entityManager, operatorContextProvider),
                new NoOperatorContextBaseRepository<UserInvitationEntity>(entityManager));
    }

    @Bean("account.userAccountServicePort")
    public UserAccountServicePort userAccountServicePort(final AuthenticationPort authenticationPort,
                                                         final UserPort userPort,
                                                         final PasswordEncoder passwordEncoder,
                                                         final OperatorContext operatorContext) {
        return new UserAccountServiceAdapter(authenticationPort, userPort, passwordEncoder, operatorContext);
    }

    @Bean
    public SupplierAccountPort supplierAccountPort(
            @Qualifier("account.userAccountServicePort")
            final UserAccountServicePort userAccountServicePort,
            final UserInvitationRepository userInvitationRepository,
            final IntegrationEventPublisher integrationEventPublisher,
            @Value("${manager.auth.invitation.validity:PT72H}") final Duration invitationValidity,
            @Value("${manager.auth.invitation.url:http://localhost:3000/set-password}") final String invitationUrl,
            @Value("${manager.auth.invitation.language:pl}") final String language) {
        return new SupplierAccountPortImpl(userAccountServicePort, userInvitationRepository, integrationEventPublisher,
                Clock.systemUTC(), invitationValidity, invitationUrl, language);
    }
}
