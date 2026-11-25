package com.warehouse.billing.pricing.configuration;

import com.warehouse.billing.api.PriceQuoteApiService;
import com.warehouse.billing.pricing.application.port.primary.PricingPort;
import com.warehouse.billing.pricing.application.port.primary.PricingPortImpl;
import com.warehouse.billing.pricing.application.port.secondary.PriceListRepository;
import com.warehouse.billing.pricing.application.port.secondary.PriceQuoteRepository;
import com.warehouse.billing.pricing.domain.service.PriceCalculator;
import com.warehouse.billing.pricing.domain.service.PriceListFactory;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.PriceQuoteApiServiceAdapter;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.mapper.PricingWebMapper;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.PriceListRepositoryImpl;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.PriceQuoteRepositoryImpl;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity.PriceListEntity;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity.PriceQuoteEntity;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.mapper.PricingPersistenceMapper;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.NoOperatorContextBaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class PricingConfiguration {

    @Bean("priceListBaseRepository")
    public OperatorFilteredRepository<PriceListEntity> priceListBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean("priceQuoteBaseRepository")
    public OperatorFilteredRepository<PriceQuoteEntity> priceQuoteBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public PricingPersistenceMapper pricingPersistenceMapper() {
        return new PricingPersistenceMapper();
    }

    @Bean
    public PriceListRepository priceListRepository(
            @Qualifier("priceListBaseRepository")
            final OperatorFilteredRepository<PriceListEntity> repository,
            final EntityManager entityManager,
            final PricingPersistenceMapper pricingPersistenceMapper) {
        return new PriceListRepositoryImpl(repository, new NoOperatorContextBaseRepository<>(entityManager),
                pricingPersistenceMapper);
    }

    @Bean
    public PriceQuoteRepository priceQuoteRepository(
            @Qualifier("priceQuoteBaseRepository")
            final OperatorFilteredRepository<PriceQuoteEntity> repository,
            final PricingPersistenceMapper pricingPersistenceMapper) {
        return new PriceQuoteRepositoryImpl(repository, pricingPersistenceMapper);
    }

    @Bean
    public PricingPort pricingPort(final PriceListRepository priceListRepository,
                                   final PriceQuoteRepository priceQuoteRepository) {
        return new PricingPortImpl(priceListRepository, priceQuoteRepository, new PriceCalculator(),
                new PriceListFactory(), Clock.systemUTC());
    }

    @Bean
    public PricingWebMapper pricingWebMapper() {
        return new PricingWebMapper();
    }

    @Bean
    public PriceQuoteApiService priceQuoteApiService(final PricingPort pricingPort,
                                                     final PricingWebMapper pricingWebMapper) {
        return new PriceQuoteApiServiceAdapter(pricingPort, pricingWebMapper);
    }
}
