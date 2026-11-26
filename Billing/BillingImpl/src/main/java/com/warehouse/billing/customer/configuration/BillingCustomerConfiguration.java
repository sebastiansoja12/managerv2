package com.warehouse.billing.customer.configuration;

import com.warehouse.auth.UserApiService;
import com.warehouse.billing.api.BillingCustomerApiService;
import com.warehouse.billing.customer.application.port.primary.BillingCustomerPort;
import com.warehouse.billing.customer.application.port.primary.BillingCustomerPortImpl;
import com.warehouse.billing.customer.application.port.secondary.BillingCustomerRepository;
import com.warehouse.billing.customer.application.port.secondary.BillingCustomerUserServicePort;
import com.warehouse.billing.customer.domain.service.BillingCustomerFactory;
import com.warehouse.billing.customer.infrastructure.adapter.primary.BillingCustomerApiServiceAdapter;
import com.warehouse.billing.customer.infrastructure.adapter.primary.mapper.BillingCustomerWebMapper;
import com.warehouse.billing.customer.infrastructure.adapter.secondary.BillingCustomerRepositoryImpl;
import com.warehouse.billing.customer.infrastructure.adapter.secondary.BillingCustomerUserServiceAdapter;
import com.warehouse.billing.customer.infrastructure.adapter.secondary.entity.BillingCustomerEntity;
import com.warehouse.billing.customer.infrastructure.adapter.secondary.mapper.BillingCustomerPersistenceMapper;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class BillingCustomerConfiguration {

    @Bean("billingCustomerBaseRepository")
    public OperatorFilteredRepository<BillingCustomerEntity> billingCustomerBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public BillingCustomerRepository billingCustomerRepository(
            @Qualifier("billingCustomerBaseRepository")
            final OperatorFilteredRepository<BillingCustomerEntity> repository) {
        return new BillingCustomerRepositoryImpl(repository, new BillingCustomerPersistenceMapper());
    }

    @Bean
    public BillingCustomerUserServicePort billingCustomerUserServicePort(
            final UserApiService userApiService,
            final OperatorContextProvider operatorContextProvider) {
        return new BillingCustomerUserServiceAdapter(userApiService, operatorContextProvider);
    }

    @Bean
    public BillingCustomerPort billingCustomerPort(final BillingCustomerRepository billingCustomerRepository,
                                                   final BillingCustomerUserServicePort billingCustomerUserServicePort) {
        return new BillingCustomerPortImpl(billingCustomerRepository, billingCustomerUserServicePort,
                new BillingCustomerFactory(), Clock.systemUTC());
    }

    @Bean
    public BillingCustomerWebMapper billingCustomerWebMapper() {
        return new BillingCustomerWebMapper();
    }

    @Bean
    public BillingCustomerApiService billingCustomerApiService(final BillingCustomerPort billingCustomerPort,
                                                               final BillingCustomerWebMapper billingCustomerWebMapper) {
        return new BillingCustomerApiServiceAdapter(billingCustomerPort, billingCustomerWebMapper);
    }
}
