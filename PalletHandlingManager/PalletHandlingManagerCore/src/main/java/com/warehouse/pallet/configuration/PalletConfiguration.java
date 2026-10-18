package com.warehouse.pallet.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.pallet.domain.port.primary.PalletPort;
import com.warehouse.pallet.domain.port.primary.PalletPortImpl;
import com.warehouse.pallet.domain.port.secondary.DriverRepository;
import com.warehouse.pallet.domain.port.secondary.CurrentUserServicePort;
import com.warehouse.pallet.domain.port.secondary.PalletHistoryRepository;
import com.warehouse.pallet.domain.port.secondary.PalletRepository;
import com.warehouse.pallet.infrastructure.adapter.secondary.CurrentUserServiceAdapter;
import com.warehouse.pallet.infrastructure.adapter.secondary.PalletHistoryRepositoryImpl;
import com.warehouse.pallet.domain.service.DriverStorageService;
import com.warehouse.pallet.domain.service.PalletStorageService;
import com.warehouse.pallet.domain.service.SealNumberService;
import com.warehouse.pallet.domain.service.SealNumberServiceImpl;
import com.warehouse.pallet.domain.service.PalletTrackingNumberGenerator;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.warehouse.pallet.infrastructure.adapter.secondary.DriverRepositoryImpl;
import com.warehouse.pallet.infrastructure.adapter.secondary.PalletDocumentReadRepository;
import com.warehouse.pallet.infrastructure.adapter.secondary.PalletRepositoryImpl;

@Configuration
public class PalletConfiguration {


    @Bean
    public PalletPort palletPort(final PalletStorageService palletStorageService,
                                 final DriverStorageService driverStorageService,
                                 final SealNumberService sealNumberService,
                                 final PalletHistoryRepository palletHistoryRepository,
                                 final CurrentUserServicePort currentUserServicePort) {
        return new PalletPortImpl(palletStorageService, driverStorageService, sealNumberService,
                palletHistoryRepository, currentUserServicePort);
    }

    @Bean
    public PalletHistoryRepository palletHistoryRepository(final MongoTemplate mongoTemplate) {
        return new PalletHistoryRepositoryImpl(mongoTemplate);
    }

    @Bean
    public CurrentUserServicePort currentUserServicePort() {
        return new CurrentUserServiceAdapter();
    }

    @Bean
    public SealNumberService sealNumberService(final PalletRepository palletRepository) {
        return new SealNumberServiceImpl(palletRepository);
    }

    @Bean
    public PalletRepository palletRepository(final PalletDocumentReadRepository repository,
                                             final MongoTemplate mongoTemplate) {
        return new PalletRepositoryImpl(repository, mongoTemplate);
    }

    @Bean
    public PalletTrackingNumberGenerator palletTrackingNumberGenerator() {
        return new PalletTrackingNumberGenerator();
    }

    @Bean
    public PalletMongoIndexInitializer palletMongoIndexInitializer(
            final MongoTemplate mongoTemplate,
            final PalletTrackingNumberGenerator palletTrackingNumberGenerator) {
        return new PalletMongoIndexInitializer(mongoTemplate, palletTrackingNumberGenerator);
    }

    @Bean
    public DriverRepository driverRepository() {
        return new DriverRepositoryImpl();
    }
}
